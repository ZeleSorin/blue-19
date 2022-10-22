package server;

public interface GameServer {

    /**
     * Starts the server using the port provided in the constructor.
     */
    void start();

    /**
     * Returns the port number of the server.
     * @return port.
     */
    int getPort();

    /**
     * Stops the server.
     */
    void stop();
}
