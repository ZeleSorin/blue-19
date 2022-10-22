package server;

import board.Board;
import mark.Mark;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import player.ServerPlayer;
import strategy.SmartStrategy;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


import java.util.Scanner;


public class Client implements GameClient, Runnable {

    //<-------------------------------------------------------------------->
//<------------------BLOCK WITH VARIABLE DECLARATION------------------->
    /**
     * Socket that will be used to connect to the server.
     */
    private Socket socket = null;

    /**
     * The main writer of this object.
     */
    private BufferedWriter writer = null;

    /**
     *Thread that is gonna run at the moment a connection is gonna be established.
     * This thread will run this object.
     */
    private Thread a1;

    /**
     *The description of this client.
     */
    private final String description = "SorinAndBram";

    /**
     *String array containing the extensions.
     */
    private final String[] extentions = {};

    /**
     *Boolean that states if the login was accepted.
     */
    private boolean loginAccepted = false;

    /**
     *Boolean that states if we are in a queue.
     */
    protected boolean inQueue = false;

    /**
     *String that contains this client's username.
     */
    private String userName;

    /**
     *Boolean that states the server response. If the server responded to our
     * commands, it becomes true then false again.
     */
    private boolean serverResponse = false;

    /**
     *Boolean connected that states if we are still connected to the server.
     */
    private boolean connected = true;

    /**
     *Boolean that states if ve are playing a game.
     */
    private boolean playingGame = false;

    /**
     *String list that is going to contain the list of the clients in the server.
     */
    private String list = null;

    /**
     *Scanner object used for taking input from the user.
     */
    private Scanner scanner;

    /**
     *Mode integer that checks the mode the user is in.
     */
    private int mode = 1;

    /**
     * Integer Move that holds a move.
     */
    int move;

    /**
     * Integer rotation that holds a rotation.
     */
    int rotation;

    /**
     *ServerGame object that is going to point to a new ServerGame object created when
     * we receive NEWGAME.
     */
    private ServerGame newGame;

    /**
     *Player object that is going to hold this player.
     */
    private Player thisPlayer;

    /**
     *ServerPlayer object that is going to hold the opponent player.
     */
    private ServerPlayer serverPlayer;

    /**
     *gameThread that is going run the newGame object.
     */
    private Thread gameThread;

    /**
     *Board object that is gonna hold this board.
     */
    private Board board;

//<-------------------------------------------------------------------->
//<-----------------BLOCK WITH CONSTRUCTOR DECLARATION----------------->


//<-------------------------------------------------------------------->
//<-------------------BLOCK WITH SETTERS AND GETTERS------------------->


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isPlayingGame() {
        return playingGame;
    }

    public Thread getGameThread() {
        return gameThread;
    }

    public void setGameThread(Thread gameThread) {
        this.gameThread = gameThread;
    }

    public String[] getExtentions() {
        return extentions;
    }

    public boolean isServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(boolean serverResponse) {
        this.serverResponse = serverResponse;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public void setThisPlayer(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public void setUsername(String username) {
        this.userName = username;
    }

    public String getUsername() {
        return this.userName;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean bool) {
        connected = bool;
    }

    public void setPlayingGame(boolean bool) {
        this.playingGame = bool;
    }

    public boolean getPlayingGame() {
        return this.playingGame;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public void setMode(String num) {
        this.mode = Integer.parseInt(num);
    }

    public int getMode() {
        return this.mode;
    }

//<-------------------------------------------------------------------->
//<-----------------BLOCK WITH MAIN METHOD DECLARATION----------------->
//<-------------------------------------------------------------------->

    @Override
    public void run() {
        try (var buf = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String newLine;
            // This loop will read all the commands received from the server.
            while ((newLine = buf.readLine()) != null) {
                //System.out.println("INPUT: " + newLine);
                String[] commands = newLine.split("~");
                switch (commands[0]) {
                    case "HELLO":

                        System.out.println("Connected with server : " + commands[1]);

                        serverResponse = true;

                        break;
                        // When we receive move, we set the move and rotation to the received commands and
                        // assign the serverPlayer with this commands.
                    case "MOVE":
                        if (playingGame) {
                            move = Integer.parseInt(commands[1]);
                            rotation = Integer.parseInt(commands[2]);


                            serverPlayer.setMove(move);
                            serverPlayer.setRotation(rotation);

                            serverResponse = true; // serverResponse always becomes true after receiving a command.
                            break;
                        } else {
                            break;
                        }


                    case "LOGIN":
                        loginAccepted = true;
                        serverResponse = true;
                        break;

                    case "ALREADYLOGGEDIN":
                        loginAccepted = false;
                        serverResponse = true;
                        break;

                    case "LIST":// After receiving LIST from the server, we add each element
                        //after LIST into a formated String and display it in a fancy way.
                        list = "The players in this server are: \n";
                        for (int i = 1; i < commands.length; i++) {
                            list +=  i + ". " + commands[i] + "\n";
                        }
                        serverResponse = true;
                        break;

                    case "NEWGAME":
                        /*
                        When receiving a NEWGAME command we check if we are already in a game.
                        */
                        if (getPlayingGame()) {
                            sendData("ERROR~Already in game");
                            break;
                        }
                        /*
                        If we are not in a game, we print a message and set the playingGame boolean to true.
                         */
                        System.out.println("We are starting a new game");
                        setPlayingGame(true);
                        /*
                        If the first username is our username, we assign the server player as player two.
                         */
                        if (commands[1].equals(getUsername())) {
                            setServerPlayer(new ServerPlayer(commands[2], Mark.BLACK));

                            /*
                            Depending on the MODE we are into, we assign this player a
                            computer player or human player.
                             */
                            switch (getMode()) {
                                case 3:
                                    thisPlayer = new ComputerPlayer(Mark.WHITE,
                                            new SmartStrategy());
                                    break;
                                case 2:
                                    thisPlayer = new ComputerPlayer(Mark.WHITE);
                                    break;
                                default:
                                    thisPlayer = new HumanPlayer(commands[1],
                                            Mark.WHITE, getScanner());
                                    break;
                            }
                            /*
                            We assign the newGame object with a newServerGame containing this two players,
                            a scanner and this client.
                             */
                            newGame = new ServerGame(getThisPlayer(),
                                    getServerPlayer(), getScanner(), this);
                            /*
                            We set the gameThread to run the newGame object
                             */
                            setGameThread(new Thread(newGame));
                            getGameThread().start();
                            System.out.println("Game started, you have to move first");

                        } else {
                            /*
                            If we are not the firstplayer we do everything in reverse.
                             */
                            setServerPlayer(new ServerPlayer(commands[1], Mark.WHITE));

                            switch (getMode()) {
                                case 3:
                                    thisPlayer = new ComputerPlayer(Mark.BLACK,
                                            new SmartStrategy());
                                    break;
                                case 2:
                                    thisPlayer = new ComputerPlayer(Mark.BLACK);
                                    break;
                                default:
                                    thisPlayer = new HumanPlayer(commands[2],
                                            Mark.BLACK, getScanner());
                                    break;
                            }

                            newGame = new ServerGame(getServerPlayer(),
                                    getThisPlayer(), getScanner(), this);

                            setGameThread(new Thread(newGame));
                            getGameThread().start();
                            System.out.println("Game started, other player has to move fist.");

                        }
                        break;

                    case "PING":
                        System.out.println("You were PINGed");
                        pong();
                        break;

                    case "PONG":
                        System.out.println("You received a PONG ");
                        break;

                    case "QUIT":
                        sendData("QUIT");
                        setServerResponse(true);
                        break;

                    case "GAMEOVER":
                        /*
                        If we receive GAMEOVER, we check
                        what was the reason of the GAMEOVER and handle
                        each case propperly.
                         */
                        if (commands[1].equals("VICTORY")) {
                            System.out.println("Player " + commands[2] + " won!!!");
                        } else if (commands[1].equals("DISCONNECT")) {
                            System.out.println("Player " + commands[2] +
                                    " won because the opponent left the game!!!");
                        } else {
                            System.out.println("It's a draw, nobody won :(");
                        }
                        setServerResponse(true);
                        setInQueue(false);
                        setPlayingGame(false);
                        getGameThread().join();
                        System.out.println("If you wish to play another game, " +
                                "join the queue again by typing " + "'QUEUE'" +
                                " or press HELP to see the menu");
                        break;
                    case "ERROR":
                        System.out.println("ERROR: " + commands[1]);
                        this.close();
                        break;
                    default:
                        setServerResponse(true);

                        System.out.println("error at run method Client class");
                }
            }


        } catch (IOException | InterruptedException e) {

            System.out.println("something went wrong");
            this.close();
        }
        System.out.println("running method stopped");
    }

    /**
     * connect tries to set up a connection with the provided parameters, if a connection
     * is set up, it returns true, otherwise it returns false.
     *
     * @param address the server address the player wants to connect with
     * @param port the port number where the player wants to connect with
     * @return true if the connection succeeded or false if it failed
     */
    @Override
    public boolean connect(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            a1 = new Thread(this);
            a1.start();
            setConnected(true);
            return true;
        } catch (IOException e) {
            System.out.println("connection failed...");
            setConnected(false);
            return false;
        }
    }

    /**
     * closes the connection with the server.
     */
    @Override
    public void close() {
        try {
            sendData("QUIT");

            socket.close();

            a1.join();

            setConnected(false);

        } catch (IOException | InterruptedException e) {
            System.out.println("closing failed...");
        }
    }

    /**
     * this method is a special sendMessage method that sends this player's move
     * to the server, and waits on.
     * @param index the index number of a field this player want to put a mrable on.
     * @param toRotate the value for what quadrant the player want to rotate
     * @throws IOException if something went wrong in the sendData method, this throws an error
     * to the place this method is called, so it can be caught  in a personalised way.
     */
    @Override
    public void sendMove(int index, int toRotate) throws IOException {
        String command = "MOVE~" + index + "~" + toRotate;
        sendData(command);
        waitResponse();

    }

    /**
     * send data takes a String and sends it to the socket of the server it's connected to.
     *
     * @param data is the message that will be sent to the server
     * @throws IOException if something goes wrong with sending the message, other methods
     * that use sendData can catch it, so they can send a personalised error message.
     */
    @Override
    public void sendData(String data) throws IOException {
        writer.write(data);
        writer.newLine();
        writer.flush();
        //System.out.println("OUTPUT: " + data);
    }

    /**
     * send the HELLO command to the server and also checks if the server sends
     * a hello message back.
     */
    @Override
    public void startInitialization() {
        //building the hello message with our description and our extentions
        String message = "HELLO~" + description;
        for (String s : getExtentions()) {
            message += "~" + s;
        }

        //here we actually send the message to the server.
        try {
            sendData(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error with sending the HELLO " +
                    "message from the client to the server");
        }


        //wait for a response from the server
        waitResponse();

    }

    @Override
    public void waitResponse() {

        while (true) {
            Thread.yield();
            if (serverResponse) {
                serverResponse = false;
                break;
            }
        }
    }

    /**
     * checks if the player is playing a game, stops when the player finishes the game.
     */
    @Override
    public void isPlaying() {
        while (true) {
            Thread.yield();
            if (!playingGame) {
                break;

            }
        }

    }

    /**
     * Sends the username to the server, if the username is not taken in the server,
     * it returns true, if it is taken this method returns false.
     * @param username is the name of the player
     * @return true if name is not already in server, false if it is.
     */
    @Override
    public boolean checkUsername(String username) {
        try {
            sendData("LOGIN~" + username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //wait for a response from the server
        waitResponse();

        //the response of the server is LOGIN or ALLREADYLOGGEDIN,
        //according to which message we receive, we output true or false.
        return loginAccepted;
    }

    /**
     * asks the server to put us in a queue to play a game,
     * if this is triggered while the player already is in a queue, he or she will then
     * be removed from the queue.
     * @return a message saying if the player joined or left the queue
     */
    @Override
    public String queue(String data) {
        try {
            sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!isInQueue()) {
            setInQueue(true);
            return "You are placed in the queue";
        } else {
            setInQueue(false);
            return "You left the que";
        }
    }

    /**
     * asks the server to send us a list of all the current players on that server.
     * @return all players neatly displayed in a list
     */
    @Override
    public String getList() {
        try {
            sendData("LIST");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //wait for a response from the server
        waitResponse();
        return list;
    }

    /**
     * returns the status of the player according several variables.
     * so it can be printed by the client
     *
     * @return the status of the player concerning its name, queu status,
     * mode status and gamestatus
     */
    @Override
    public String getStatus() {
        String message = "> Status of player: " + this.getUsername() + "\n";
        if (this.isInQueue()) {
            message += "> Queue status:     in queue \n";
        } else {
            message += "> Queue status:     not in queue \n";
        }

        if (this.isPlayingGame()) {
            message += "> Game status:      in game \n";
        } else {
            message += "> Game status:      not in game \n";
        }

        message += "> Mode status:      " + getModeStatus();
        return message;


    }

    /**
     * this looks at the mode variable and according to that value
     * this method returns the fitting description of that value.
     *
     * @return the description of the mode this player is in.
     */
    @Override
    public String getModeStatus() {
        if (getMode() == 1) {
            return "Human Player";
        } else if (getMode() == 2) {
            return "Naive Computer Player ";
        } else {
            return "Smart Computer Player ";
        }
    }

    /**
     * getHelp prints the list with basic commands.
     */
    @Override
    public void getHelp() {

        System.out.println("These are the possible commands:");
        System.out.println("LIST.........shows a list of users of the connected server");
        System.out.println("QUEUE........indicates that you want to participate in game");
        System.out.println(".............If it is typed a second time, you will leave the queue");
        System.out.println("STATUS.......Shows the ");
        System.out.println("QUIT.........quits the server");
        System.out.println("MODE 1/2/3...change the player mode:");
        System.out.println(".....1.......to play as a human");
        System.out.println(".......2.....to play as a Naive AI");
        System.out.println(".........3...to play as a Smart AI");

    }

    /**
     * ping sends a PING command to the server.
     */
    @Override
    public void ping() {
        try {
            sendData("PING");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * pong command sends a PONG command to the server.
     */
    @Override
    public void pong() {
        try {
            sendData("PONG");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Pong went wrong");
        }
    }


}
//<-------------------------------------------------------------------->

