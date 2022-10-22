package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Locale;
import java.util.Scanner;

public class Pentago {


    public static void main(String[] args) {
        Client mainClient = new Client();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);
        mainClient.setScanner(scanner);
        String address = null;
        int port = -1;
        String command = null;
        String username = null;
        boolean correctUsername = false;
        boolean quit = false;
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_YELLOW = "\u001B[33m";
//--------------------------------INITIALIZATION-----------------------------------------------
        try {
            //Take the user input for setting up the connection.
            System.out.println(ANSI_YELLOW + "Enter server address: (130.89.253.64)" + ANSI_RESET);
            address = reader.readLine();


            System.out.println(ANSI_YELLOW +"Enter port number: (55555)"+ ANSI_RESET);
            port = Integer.parseInt(reader.readLine());
            port = 55555;

            /*
            Enter a loop if the connection is not established. The loop will ask the user
            to re-enter the port and address until a connection can be made.
             */
            while (!mainClient.connect(InetAddress.getByName(address), port)) {
                System.out.println(ANSI_RED + "Re-enter the server address: " + ANSI_RESET);
                address = reader.readLine();
                System.out.println(ANSI_RED + "Re-enter the port number: " + ANSI_RESET);
                port = Integer.parseInt(reader.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Send a HELLO command to the server with a description
         */
        mainClient.startInitialization();
        /*
        The initialization has been done.
        The user is asked to enter his username.
        If the server sends a positive response back, the user is loggedIn, otherwise
        he has to re-enter the username.
        A positive response means that there isn't another client with this username.
         */
//--------------------------------------------------------------------------------------------
        while (!correctUsername) {
            System.out.println("Enter username:  ");
            username = scanner.nextLine();
            if (!username.contains("~")) {
                mainClient.setUsername(username);
                if (mainClient.checkUsername(username)) {
                    correctUsername = true;
                    System.out.println("Login succesfull.");
                } else {
                    System.out.println("this username is already in the server, try again");
                }
            } else {
                System.out.println("username can't contain the symbol '~', try again");
            }
        }

        // This is the main part of the Pentago class, here the user can input several commands
        // if the player types 'HELP' they can see all the possible commands
        try {
            while (!quit && mainClient.getSocket().isConnected()) {
                System.out.println("type a command: (type HELP to see all commands)");

                //check if a player is in a game
                mainClient.isPlaying();

                command = scanner.nextLine();
                String[] commands = command.toUpperCase(Locale.ROOT).split(" ");
                if (command == null) {
                    System.out.println("Null command");

                    // if a user is in a game, the user can still send one command
                    // to this switch loop, to catch that one
                    // exception, we first check if the player is in a game
                    // or not before forwarding the command
                } else if (!mainClient.getPlayingGame()) {

                    switch (commands[0]) {
                        //prints the help menu
                        case "HELP":
                            mainClient.getHelp();
                            break;

                        //gets a list of all the players on the server this player is on
                        case "LIST":
                            System.out.println(mainClient.getList());
                            break;


                        //places the user in the queue to play a game
                        //if queue is typed while the player is in a queue
                        //he or she will be placed out of the queue
                        case "QUEUE":
                            System.out.println(mainClient.queue("QUEUE"));
                            break;

                        //breaks the connection with the server, and terminates this program
                        case "QUIT":
                            System.out.println("quiting server...");
                            quit = true;
                            mainClient.close();
                            break;

                        //send a ping message to the server, if the connection still stands.
                        //the user should receive a PONG message
                        case "PING":
                            mainClient.ping();
                            break;

                        //to change to Mode the player is playing in,
                        //the player can play as a human or as a Naive/Smart AI
                        case "MODE":
                            if (commands.length == 2 && (commands[1].equals("1")
                                    || commands[1].equals("2")
                                    || commands[1].equals("3"))) {
                                mainClient.setMode(commands[1]);
                                System.out.println("Mode set to " + commands[1]);
                            } else {
                                System.out.println("If you want to change the mode " +
                                        "write it like this:");
                                System.out.println("'MODE 1' to play as a human");
                                System.out.println("'MODE 2' to play as a simple computer");
                                System.out.println("'MODE 3' to play as a smart computer");
                            }
                            System.out.println("you are now playing as a "
                                    + mainClient.getModeStatus());
                            break;

                        //prints the status of the player such as the mode, the queue status
                        //and if they are in game or not
                        case "STATUS":
                            System.out.println(mainClient.getStatus());
                            break;
                        default:
                            System.out.println("this command is unknown, type 'HELP' " +
                                    "to see all possible commands");
                    }
                }
            }

        } catch (Exception e) {
            mainClient.close();
            System.out.println("quiting server...");
        }


    }

}
