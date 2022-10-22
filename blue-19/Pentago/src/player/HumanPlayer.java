package player;

import board.Board;
import mark.Mark;
import strategy.SmartStrategy;

import java.util.Locale;
import java.util.Scanner;

public class HumanPlayer extends Player {

    private int toMove;
    private int toRotate;
    private Scanner scanner;

    public int getMove() {
        return toMove;
    }

    public int getRotation() {
        return toRotate;
    }

    public void setMove(int move) {
        this.toMove = move;
    }

    public void setRotation(int rotation) {
        this.toRotate = rotation;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * creates a new player object with a name and a mark.
     *
     * @param name is the name of player.
     * @param mark is the mark the player is going to use (Black or White).
     */
    /*@ requires name != null;
        requires mark == Mark.WHITE || mark == Mark.BLACK;
    @*/
    public HumanPlayer(String name, Mark mark, Scanner scanner) {
        super(name, mark);
        setScanner(scanner);
    }

    @Override
    public int determineMove(Board board) {
        Scanner sc = new Scanner(System.in);
        String choice;
        int intChoice = -1;
        boolean correctMove = false;
        String prompt = "> " + getName() + " (" + getMark().toString() + ")"
                + ", where do you want to place your marble? \n" +
                "> Type 'HINT' to receive a hint";

        while (!correctMove) {
            System.out.println(prompt);
            choice = sc.nextLine();
            if (choice.toUpperCase(Locale.ROOT).equals("QUIT")) {
                return 666;
            } else if (choice.equals("HINT")) {
                System.out.println("According to our AI placing you marble on field: " +
                        new SmartStrategy().determineMove(board, this.getMark())
                        + " is the best move.");
            } else {
                if (isInt(choice)) {
                    intChoice = Integer.parseInt(choice);
                    if (board.isField(intChoice) && board.isEmptyField(intChoice)) {
                        correctMove = true;
                    } else {
                        System.out.println("ERROR: field " + choice
                                + " is no valid choice, choose a number between 0 and 35");
                    }
                } else {
                    System.out.println("ERROR: field " + choice
                            + " is no valid choice, choose a number");
                }
            }
        }
        setMove(intChoice);
        return intChoice;
    }

    @Override
    public int determineRotation(Board board) {
        Scanner sc = new Scanner(System.in);
        String quadrant;
        int intQuadrant = -1;
        String direction;
        int intDirection = -1;
        boolean goodQuadrant = false;
        boolean goodDirection = false;
        String promptQuadrant = "> " + getName() + " (" + getMark().toString() + ")"
                + ", what quadrant do you want to rotate? \n " +
                "0 = topleft, 1 = topright, 2 = bottomleft, 3 = bottomright \n" +
                "> Type 'HINT' to receive a hint";
        String promptDirection = "> " + getName() + " (" + getMark().toString() + ")"
                + ", what direction do you want this quadrant to rotate? \n " +
                "'r' or 'R' for clockwise and 'l' or 'L' for counter-clockwise \n" +
                "> Type 'HINT' to receive a hint";

        while (!goodQuadrant) {
            System.out.println(promptQuadrant);
            quadrant = sc.nextLine();
            if (quadrant.equals("HINT")) {
                System.out.println("According to our AI rotating: " +
                        (new SmartStrategy().determineRotation(board, this.getMark()) / 2)
                        + " is the best move.");
            } else if (isInt(quadrant)) {
                intQuadrant = Integer.parseInt(quadrant);
                if (intQuadrant >= 0 && intQuadrant <= 3) {
                    goodQuadrant = true;
                } else {
                    System.out.println("ERROR: field " + quadrant
                            + " is no valid choice, choose a number between 0 and 3");
                }
            } else {
                System.out.println("ERROR: field " + quadrant
                        + " is no valid choice, choose a number");
            }
        }

        while (!goodDirection) {
            System.out.println(promptDirection);
            direction = sc.nextLine();
            if (direction.equals("HINT")) {
                int aiInt = new SmartStrategy().determineRotation(board, this.getMark()) % 2;
                String aiString;
                if (aiInt == 0) {
                    aiString = "'L'";
                } else {
                    aiString = "'R'";
                }
                System.out.println("According to our AI rotating in the direction of: " +
                        aiString + " is the best move.");
            } else if (direction.equals("r") || direction.equals("R")) {
                intDirection = 1;
                goodDirection = true;
            } else if (direction.equals("l") || direction.equals("L")) {
                intDirection = 0;
                goodDirection = true;
            } else {
                System.out.println("ERROR: " + direction + " is not a valid input.");
            }
        }

        return intQuadrant * 2 + intDirection;
    }

    private boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
