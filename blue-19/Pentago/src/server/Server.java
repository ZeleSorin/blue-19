package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements GameServer {
//<-------------------------------------------------------------------->
//<------------------BLOCK WITH VARIABLE DECLARATION------------------->

    /**
     * The name of the server.
     */

    private final String serverName = "BramAndSorinServer";

    /**
     * Variable declaration block. Most of them should be private
     * and the class provided with proper setters and getters when
     * necessary.
     */

    private ServerSocket mainServerSocket;

    /**
     * This server port number.
     */
    private static int mainPortNumber;


    /**
     * integer that helps with multithreading by knowing how many people are in the Queue.
     */
    private int nrOfConnections;

    Map<String, Set<ClientHandler>> games = new HashMap<>();

    /**
     * ExecutorService is a type of object that creates a thread pool that can hold a specified nr
     * of threads .
     * You can tell the Executor to run a specific thread or all threads.
     */
    private static ExecutorService pool = Executors.newFixedThreadPool(20);

    /**
     * All clients connected to the server are kept here.
     */
    private static List<ClientHandler> clients = new ArrayList<>();

    /**
     * All clients that are in a queue are kept here.
     */
    private static List<ClientHandler> clientInQueue = new ArrayList<>();

    /**
     * Just a lock used for multithreading purposes.
     * !!!ATTENTION!!! if you want to create another
     * multithreading process that requires a lock, create a new
     * one, as using this might cause errors when playing the game.
     */
    private Lock lock = new ReentrantLock();

//<-------------------------------------------------------------------->
//<-----------------BLOCK WITH CONSTRUCTOR DECLARATION----------------->

    /**
     * CONSTRUCTOR that receives a port number as an input.
     *
     * @param providedPort = port number.
     */
    public Server(int providedPort) {
        this.mainPortNumber = providedPort;
    }

//<-------------------------------------------------------------------->
//<-------------------BLOCK WITH SETTERS AND GETTERS------------------->


    public String getServerName() {
        return serverName;
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public ServerSocket getMainServerSocket() {
        return mainServerSocket;
    }

    public void setMainServerSocket(ServerSocket mainServerSocket) {
        this.mainServerSocket = mainServerSocket;

    }

    public Map<String, Set<ClientHandler>> getGames() {
        return games;
    }


    //<-------------------------------------------------------------------->
//<----------------------BLOCK WITH MAIN METHOD------------------------>

    public static void main(String[] args) {

        boolean connected = false;
        System.out.println("Enter port nr : \n");
        mainPortNumber = 55555; //scanner.nextInt();
        Server server = new Server(mainPortNumber);

        try {
            server.setMainServerSocket(new ServerSocket(mainPortNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Quick walk-trough of what the next loop does:
        //We wait for connection.
        // We get a socket connection
        // We assign that to a new clientHandler
        // We add it into the array of handlers
        // We execute the thread(we run the runnable thread which is clientHandler class)

        while (!connected) {
            System.out.println("[SERVER] waiting for connection...");
            try {
                Socket client = server.getMainServerSocket().accept();
                System.out.println("[SERVER] new connection established...");
                ClientHandler clientThread = null;
                try {
                    clientThread = new ClientHandler(client, server);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.add(clientThread);

                pool.execute(clientThread);

                System.out.println("[SERVER] there are " + clients.size()
                        + " clients connected...");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
//<-------------------------------------------------------------------->


    /**
     * getList is used externaly by the clientHandler to acces the list of the players.
     *
     * @return a list with all the connected clients.
     */
    public String getList() {
        String list = "LIST";
        for (ClientHandler client : clients) {
            list += "~" + client.getUsername();
        }
        return list;
    }

    /**
     * @param username is the username you sent to the server using the client.
     * @return false if your username is already logged in and true if it is not.
     */
    public boolean login(String username) {
        for (ClientHandler cl : clients) {
            if (cl.getUsername() != null && cl.getUsername().equals(username)) {
                return false;
            }
        }

        return true;

    }

    /**
     *
     * @param cl is the ClientHandler that wants to join the QUEUE
     * @throws IOException
     */
    public synchronized void setInQueue(ClientHandler cl) throws IOException {
        clientInQueue.add(cl);
        /*
        after adding itself to the que, the thread will increment the nrOFConnections.
         */
        nrOfConnections++;
        System.out.println("Players in que " + nrOfConnections);
        /*
        If theere are at least two players in queue, the thread will start assigning
        each of the two players in a new set.
         */
        if (nrOfConnections >= 2) {

            Set<ClientHandler> gameClients = new HashSet<>();
            String names = clientInQueue.get(0).getUsername()
                    + "~" + clientInQueue.get(1).getUsername();
            gameClients.add(clientInQueue.get(0));
            gameClients.add(clientInQueue.get(1));
            games.put(names, gameClients);
            /*
            After joining the games Set, we send NEWGAME to both of the players.
             */
            for (ClientHandler i : gameClients) {
                i.setInGame(true);
                i.setClients(gameClients);
                i.sendData(i.getOut(), "NEWGAME~"
                        + clientInQueue.get(0).getUsername()
                        + "~" + clientInQueue.get(1).getUsername());
            }
            /*
             * After sending NEWGAME we set the clients out of the queue.
             * and decrease the nrOFConnections by one twice.
             * We tried =- 2, = nrOfConnections -2 , but we allways had a bug so we
             * did it this way
             */
            setOutOfQueue(clientInQueue.get(0));
            setOutOfQueue(clientInQueue.get(0));
            nrOfConnections = nrOfConnections--;
            nrOfConnections = nrOfConnections--;

        }


    }

    /**
     * @param cl is the ClientHandler that wants to quit the QUEUE
     */
    public void setOutOfQueue(ClientHandler cl) {

        clientInQueue.remove(cl);
        nrOfConnections--;
    }

    /**
     * @param cl is the ClientHandler that checks if he is in the QUEUE.
     * @return true is it is in the QUEUE , false if it is not in the QUEUE.
     */
    public boolean isInQueue(ClientHandler cl) {
        if (clientInQueue.contains(cl)) {
            return true;
        }
        return false;
    }

    /**
     * USELESS.
     */
    @Override
    public void start() {

    }

    /**
     * Getter for the portNumber.
     *
     * @returns the localport.
     */
    @Override
    public int getPort() {
        return mainServerSocket.getLocalPort();
    }

    @Override
    public void stop() {

    }

    public void closeClient(ClientHandler cl) {
        try {
            System.out.println("Client " + cl.getUsername() +
                    " has disconnected from server");
            clients.remove(cl);

        } finally {
            System.out.println("[SERVER] there are " +
                    getClients().size() + " clients connected...");
        }
    }


//<-------------------------------------------------------------------->


}
