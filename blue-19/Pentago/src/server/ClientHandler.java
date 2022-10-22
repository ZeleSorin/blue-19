package server;

import board.Board;
import mark.Mark;

import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {
    /**
     * Server object used for accessing global data(like LIST and
     * things that a clientHandler shouldn't know).
     */
    private final Server server;

    /**
     * Board object created for checking moves that are sent trough players.
     */
    private Board chekUpBoard;

    /**
     * Socket object that stores the connection of this specific client to the server.
     */
    private final Socket client;

    /**
     * The reader of this client.
     */
    private BufferedReader in;

    /**
     * The writer of this client.
     */
    private BufferedWriter out;

    /**
     * The username of this client.
     */
    private String username;

    /**
     * The extensions of this client.
     */
    String[] extentions;


    private int move = 0;

    private Set<ClientHandler> clients;

    private boolean inGame = false;

    private String keyHolder;
    /**
     * Boolean created in order to check the initialization status.
     */
    private boolean initialization = false;

    //TODO: WHAT THE HECK IM USING THIS FOR?????
    private boolean listening = false;
    private final boolean loggedIn = false;
    private boolean waitingForGame = false;

    Lock lock = new ReentrantLock();

    //<-------------------------------------------------------------------->
//<-------------------BLOCK WITH SETTERS AND GETTERS------------------->


    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Set<ClientHandler> getClients() {
        return clients;
    }

    public void setClients(Set<ClientHandler> clients) {
        this.clients = clients;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getKeyHolder() {
        return keyHolder;
    }

    public void setKeyHolder(String keyHolder) {
        this.keyHolder = keyHolder;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public Socket getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Board getChekUpBoard() {
        return chekUpBoard;
    }

    public void setChekUpBoard(Board chekUpBoard) {
        this.chekUpBoard = chekUpBoard;
    }

    //<-------------------------------------------------------------------->
//<-----------------BLOCK WITH CONSTRUCTOR DECLARATION----------------->
    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.server = server;
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        setChekUpBoard(new Board());

    }

    //<-------------------------------------------------------------------->
//<-----------------BLOCK WITH MAIN METHOD DECLARATION----------------->
    @Override
    public void run() {
        System.out.println("Starting the initialization");

        while (!initialization) {
            try {
                String command;
                if ((command = in.readLine()) != null) {
                    String[] commands = command.split("~");
                    if (commands[0].equals("HELLO")) {
                        if (commands.length > 2) {
                            for (int i = 2; i < commands.length; i++) {
                                extentions[i] = commands[i];
                            }
                        }
                        String toSend = "HELLO~" + server.getServerName();
                        sendData(out, toSend);
                        initialization = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Initialization finalised...");

        String command;
        while (!listening) {
            try {
                if ((command = in.readLine()) != null) {
                    System.out.println("RECEIVED: " + command + " from " + this.username);
                    String[] commands = command.split("~");
                    switch (commands[0]) {

                        case "LIST":
                            sendData(out, server.getList());
                            break;

                        case "LOGIN":

                            if (server.login(commands[1])) {
                                this.setUsername(commands[1]);
                                sendData(out, "LOGIN");
                            } else {
                                sendData(out, "ALREADYLOGGEDIN");
                            }
                            break;
                        case "PING":
                            sendData(out, "PONG");
                            break;

                        case "PONG":

                            break;

                        case "QUEUE":

                            if (server.isInQueue(this)) {
                                server.setOutOfQueue(this);
                                waitingForGame = false;

                            } else {
                                server.setInQueue(this);
                                waitingForGame = true;
                            }
                            getChekUpBoard().reset();
                            break;
                        case "MOVE":
                            lock.lock();
                            for (String key : server.getGames().keySet()) {
                                String[] names = key.split("~");
                                if (names[0].equals(this.username) ||
                                        names[1].equals(this.username)) {
                                    setKeyHolder(key);
                                    if (isValidMove(command) && clients.contains(this)) {

                                        for (ClientHandler ch : clients) {
                                            sendData(ch.getOut(), command);
                                            ch.setMove(command);
                                        }
                                    } else {
                                        for (ClientHandler ch : clients) {

                                            sendData(ch.getOut(), "ERROR~ILLEGAL MOVE!");
                                        }
                                        System.out.println("ERROR: ILLEGAL MOVE");

                                    }
                                    if (getChekUpBoard().gameOver()) {

                                        String message = "GAMEOVER~";
                                        if (getChekUpBoard().isDraw()) {
                                            message += "DRAW";
                                        } else if (getChekUpBoard().hasWinner()) {
                                            message += "VICTORY~";
                                            //if move is odd, white (player 1) won,
                                            // if move is even, black (player 2) won
                                            if (move % 2 == 1) {
                                                message += names[0];
                                            } else {
                                                message += names[1];
                                            }
                                        }
                                        for (ClientHandler ch : clients) {
                                            sendData(ch.getOut(), message);
                                            server.getGames().remove(keyHolder);
                                            setMove(0);
                                            getChekUpBoard().reset();
                                        }
                                    }
                                }

                            }
                            lock.unlock();


                            break;

                        case "NEWGAME":

                            break;

                        case "QUIT":
                            server.closeClient(this);
                            listening = true;
                            if (server.isInQueue(this)) {
                                server.setOutOfQueue(this);
                            }
                            if (isInGame()) {
                                for (ClientHandler ch : clients) {
                                    if (!ch.getUsername().equals(this.getUsername())) {
                                        System.out.println("---------we do get here-----");
                                        sendData(ch.out, "GAMEOVER~DISCONNECT~" + ch.getUsername());
                                    }
                                }
                            }
                            server.closeClient(this);
                            this.client.close();
                            Thread.currentThread().join();
                            break;

                        default:
                            System.out.println("Something ");
                            break;

                    }
                } else {
                    //this has to be done when a connection
                    // is lost between client handler and client

                    client.close();
                    return;
                }
            } catch (IOException | InterruptedException e) {
                try {
                    if (server.isInQueue(this)) {
                        server.setOutOfQueue(this);
                    }
                    if (isInGame()) {
                        for (ClientHandler ch : clients) {
                            if (!ch.getUsername().equals(this.getUsername())) {

                                sendData(ch.out, "GAMEOVER~DISCONNECT~" + ch.getUsername());
                            }
                        }
                    }

                    server.closeClient(this);
                    Thread.currentThread().join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isValidMove(String data) {
        String[] splintedMove = data.split("~");
        int m = Integer.parseInt(splintedMove[1]);
        int r = Integer.parseInt(splintedMove[2]);
        return getChekUpBoard().isEmptyField(m) && r >= 0 && r < 8;
    }

    public void setMove(String data) {
        String[] splintedMove = data.split("~");
        int m = Integer.parseInt(splintedMove[1]);
        int r = Integer.parseInt(splintedMove[2]);
        if (move % 2 == 0) {
            getChekUpBoard().setField(m, Mark.WHITE);
        } else {
            getChekUpBoard().setField(m, Mark.BLACK);
        }
        getChekUpBoard().rotateBoard(r);
        move++;
    }

    /**
     * @param writer is the writer where the data is sent to .
     * @param data   is the command that needs to be sent to the client.
     */
    public void sendData(BufferedWriter writer, String data) {
        try {
            writer.write(data);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("error line 310 clientHandelr");
        }
    }
}
