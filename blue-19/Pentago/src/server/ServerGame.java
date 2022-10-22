package server;

import board.Board;
import player.Player;
import player.ServerPlayer;

import java.io.IOException;
import java.util.Scanner;

public class ServerGame implements Runnable {

    //-----variables
    Player p1;
    Player p2;
    Scanner scanner;
    Client mainClient;

    Board board = new Board();

    //-----getters/setters
    public Board getBoard() {
        return this.board;
    }
    //-----constructor


    public ServerGame(Player player1, Player player2, Scanner scan, Client client) {
        this.p1 = player1;
        this.p2 = player2;
        this.scanner = scan;
        this.mainClient = client;
    }

    //-----run method
    @Override
    public void run() {
        getBoard().reset();
        while (mainClient.getPlayingGame()) {
            update();
            if (p1 instanceof ServerPlayer) {
                //wait for a response from the server
                if (!waitMove()) {
                    return;
                }
            }
            System.out.println(p1.getName() + "'s turn:-------------------------------");
            if (!p1.makeMove(getBoard())) {
                update();
                if (!p1.makeRotation(getBoard())) {
                    update();
                } else {
                    try {
                        System.out.println("We quited");
                        mainClient.sendData("QUIT");
                        mainClient.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    System.out.println("We quited");
                    mainClient.sendData("QUIT");
                    mainClient.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!(p1 instanceof ServerPlayer)) {
                try {
                    mainClient.sendMove(p1.getMove(), p1.getRotation());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (getBoard().gameOver()) {
                return;
            }

            if (p2 instanceof ServerPlayer) {
                //wait for a response from the server
                if (!waitMove()) {
                    return;
                }
            }
            System.out.println(p2.getName() + "'s turn:-------------------------------");
            if (!p2.makeMove(getBoard())) {
                update();
                if (!p2.makeRotation(getBoard())) {
                    update();
                } else {
                    try {
                        System.out.println("We quited");
                        mainClient.sendData("QUIT");
                        mainClient.close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    System.out.println("We quited");
                    mainClient.sendData("QUIT");
                    mainClient.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!(p2 instanceof ServerPlayer)) {
                try {
                    mainClient.sendMove(p2.getMove(), p2.getRotation());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (getBoard().gameOver()) {
                return;
            }
        }
    }

    /**
     * Prints the game situation.
     */
    public void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()
                + "\n");
    }


    /**
     * Prints the result of the last game. <br>
     */
    //@ requires board.hasWinner() || board.isFull();
    public void printResult() {
        Player winner;

        if (board.hasWinner()) {

            if (board.isWinner(p1.getMark())) {
                winner = p1;
            } else {
                winner = p2;
            }

            if (board.isWinner(p1.getMark()) && board.isWinner(p2.getMark())) {
                System.out.println("Draw. There is no winner! :(");
            } else if (winner.getName().equals("MaxVerstappen")) {
                System.out.println("Player " + winner.getName() + " is the new F1 WORLD CHAMPION");
            } else {
                System.out.println("Player " + winner.getName() + " has won!");
            }
        } else {
            System.out.println("Draw. There is no winner! :(");
        }
    }

    public boolean waitMove() {
        while (true) {
            Thread.yield();
            if (mainClient.getPlayingGame()) {
                mainClient.waitResponse();
                return true;
            } else {
                return false;
            }
        }
    }
}
