package server;

import java.io.IOException;
import java.net.InetAddress;

public interface GameClient {
    /**
     * connect tries to set up a connection with the provided parameters, if a connection
     * is set up, it returns true, otherwise it returns false.
     *
     * @param address the server address the player wants to connect with
     * @param port the port number where the player wants to connect with
     * @return true if the connection succeeded or false if it failed
     */
    boolean connect(InetAddress address, int port);

    /**
     * closes the connection with the server.
     */
    void close();

    /**
     * this method is a special sendMessage method that sends this player's move
     * to the server, and waits on.
     * @param index the index number of a field this player want to put a mrable on.
     * @param toRotate the value for what quadrant the player want to rotate
     * @throws IOException if something went wrong in the sendData method, this throws an error
     * to the place this method is called, so it can be caught  in a personalised way.
     */
    void sendMove(int index, int toRotate) throws IOException;

    /**
     * send data takes a String and sends it to the socket of the server it's connected to.
     *
     * @param data is the message that will be sent to the server
     * @throws IOException if something goes wrong with sending the message, other methods
     * that use sendData can catch it, so they can send a personalised error message.
     */
    void sendData(String data) throws IOException;

    /**
     * send the HELLO command to the server and also checks if the server sends
     * a hello message back.
     */
    void startInitialization();

    /**
     * this method keeps on running until the server receives a message.
     */
    void waitResponse();

    /**
     * checks if the player is playing a game, stops when the player finishes the game.
     */
    void isPlaying();

    /**
     * Sends the username to the server, if the username is not taken in the server,
     * it returns true, if it is taken this method returns false.
     * @param username is the name of the player
     * @return true if name is not already in server, false if it is.
     */
    boolean checkUsername(String username);

    /**
     * asks the server to send us a list of all the current players on that server.
     * @return all players needly displayed in a list
     */
    String getList();

    /**
     * asks the server to put us in a queue to play a game,
     * if this is triggered while the player already is in a queue, he or she will then
     * be removed from the queue.
     * @return a message saying if the player joined or left the queue
     */
    String queue(String data);

    /**
     * returns the status of the player according several variables.
     * so it can be printed by the client
     *
     * @return the status of the player concerning its name, queu status,
     * mode status and gamestatus
     */
    String getStatus();

    /**
     * this looks at the mode of a player and according to that value
     * this method returns the fitting description of that value.
     *
     * @return the description of the mode this player is in.
     */
    String getModeStatus();

    /**
     * getHelp prints the list with basic commands.
     */
    void getHelp();

    /**
     * sends ping message to the server, it expects a pong message back from the server.
     */
    void ping();

    /**
     * sends a pong message to the server, as a reaction to a received ping message.
     */
    void pong();
}
