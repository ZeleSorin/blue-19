package board;

import mark.Mark;


public class Board {

    private static final int DIM = 6;
    private static final String SPACE = "       ";
    private static final String[] NUMBERING = {" 0 | 1 | 2 || 3 | 4 | 5 ",
                                               "---+---+---++---+---+---",
                                               " 6 | 7 | 8 || 9 |10 |11 ",
                                               "---+---+---++---+---+---",
                                               "12 |13 |14 ||15 |16 |17 ",
                                               "===+===+===++===+===+===",
                                               "18 |19 |20 ||21 |22 |23 ",
                                               "---+---+---++---+---+---",
                                               "24 |25 |26 ||27 |28 |29 ",
                                               "---+---+---++---+---+---",
                                               "30 |31 |32 ||33 |34 |35 "};
    private static final String SMALLLINE = NUMBERING[1];
    private static final String BIGLINE = NUMBERING[5];


    public Mark[] fields = new Mark[DIM * DIM];

    //Constructor

    public Board() {
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = Mark.EMPTY;
        }
    }

    /**
     * Returns the content of the field referred to by the (row,col) pair.
     *
     * @param row the row of the field
     * @param col the column of the field
     * @return the mark on the field
     */
    /*@ requires isField(row, col);
    ensures \result == Mark.EMPTY || \result == Mark.BLACK || \result == Mark.WHITE;
     @*/
    /*@ pure */public Mark getField(int row, int col) {
        return fields[(DIM * row) + col];
    }

    /**
     * Creates a deep copy of this field.
     */
    /*@ ensures \result != this;
     ensures (\forall int i; (i >= 0 && i < DIM*DIM); \result.fields[i] == this.fields[i]);
     @*/
    /*@ pure */public Board deepCopy() {
        Board copyBoard = new Board();
        for (int i = 0; i < DIM * DIM; i++) {
            copyBoard.fields[i] = this.fields[i];
        }

        return copyBoard;
    }

    /**
     * Calculates the index in the linear array of fields from a (row, col)
     * pair.
     *
     * @return the index belonging to the (row,col)-field
     */
    /*@ requires row >= 0 && row < DIM;
    requires col >= 0 && row < DIM;
     @*/
    /*@ pure */public int index(int row, int col) {
        return DIM * row + col;
    }


    /**
     * Returns true if index is a valid index of a field on the board.
     *
     * @return true if 0 <= index < DIM*DIM
     */
    //@ ensures index >= 0 && index < DIM*DIM ==> \result == true;
    /*@ pure */public boolean isField(int index) {
        return index >= 0 && index < (DIM * DIM);
    }


    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     *
     * @return true if 0 <= row < DIM && 0 <= col < DIM
     */
    //@ ensures row >= 0 && row < DIM && col >= 0 && col < DIM ==> \result == true;
    /*@ pure */public boolean isField(int row, int col) {
        return row >= 0 && row < DIM && col >= 0 && col < DIM;
    }


    /**
     * Returns the content of the field i.
     *
     * @param i the number of the field (see NUMBERING)
     * @return the mark on the field
     */
    /*@ requires isField(i);
    ensures \result == Mark.EMPTY || \result == Mark.WHITE || \result == Mark.BLACK;
     @*/
    /*@ pure */public Mark getField(int i) {
        return fields[i];

    }


    /**
     * Returns true if the field i is empty.
     *
     * @param i the index of the field (see NUMBERING)
     * @return true if the field is empty
     */
    /*@ requires isField(i);
    ensures getField(i) == Mark.EMPTY ==> \result == true;
     @*/
    /*@ pure */public boolean isEmptyField(int i) {
        return getField(i) == Mark.EMPTY;

    }


    /**
     * Returns true if the field referred to by the (row,col) pair it empty.
     *
     * @param row the row of the field
     * @param col the column of the field
     * @return true if the field is empty
     */
    /*@ requires isField(row, col);
    ensures getField(row, col) == Mark.EMPTY ==> \result == true;
     @*/
    /*@ pure */public boolean isEmptyField(int row, int col) {
        return getField((DIM * row) + col) == Mark.EMPTY;

    }


    /**
     * Tests if the whole board is full.
     *
     * @return true if all fields are occupied
     */
    /*@
        @ ensures (\forall int i; (i >= 0 && i < DIM*DIM);
        fields[i] == Mark.WHITE || fields[i] == Mark.BLACK);
    */
    /*@ pure */public boolean isFull() {
        for (int i = 0; i < DIM * DIM; i++) {
            if (getField(i) == Mark.EMPTY) {
                return false;
            }
        }
        return true;

    }


    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the whole board is full.
     *
     * @return true if the game is over
     */
    //@ ensures isDraw() || hasWinner() ==> \result == true;
    /*@ pure */public boolean gameOver() {
        return isDraw() || hasWinner();

    }


    /**
     * Checks whether there is a row which is full and only contains the mark
     * m.
     *
     * @param m the Mark of interest
     * @return true if there is a row controlled by m
     */
    /*@
        requires m == Mark.WHITE || m == Mark.BLACK;
    */
    /*@ pure */public boolean hasRow(Mark m) {
        int check = 0;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (getField(i * DIM + j) == m) {
                    check++;
                }
            }
            if (check == DIM - 1 &&
                    (getField(i * DIM) != m ||
                            getField(i * DIM + (DIM - 1)) != m) ||
                    check == DIM) {
                return true;
            } else {
                check = 0;
            }
        }
        return false;
    }


    /**
     * Checks whether there is a column which is full and only contains the mark
     * m.
     *
     * @param m the Mark of interest
     * @return true if there is a column controlled by m
     */
    /*@
        requires m == Mark.WHITE || m == Mark.BLACK;
    */
    /*@ pure */public boolean hasColumn(Mark m) {
        int check = 0;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (getField(i + DIM * j) == m) {
                    check++;
                }

            }
            if (check == DIM - 1 &&
                    ((getField(i) != m || getField(DIM * (DIM - 1) + i) != m))
                    || check == DIM) {
                return true;
            } else {
                check = 0;
            }

        }
        return false;

    }


    /**
     * Checks whether there is a diagonal which is full and only contains the
     * mark m.
     *
     * @param m the Mark of interest
     * @return true if there is a diagonal controlled by m
     */
    /*@
        requires m == Mark.WHITE || m == Mark.BLACK;
    */
    /*@ pure */public boolean hasDiagonal(Mark m) {
        boolean correctDiagonal = false;
        int left = 0;
        int right = 0;

        //checking for top left to bottom right (all the three ways)
        for (int i = 0; i < DIM; i++) {
            if (getField(i, i) == m) {
                left++;
            }
        }
        if (left == 6 || left == 5 && (getField(0, 0) != m || getField(DIM - 1, DIM - 1) != m)) {
            correctDiagonal = true;
        }
        left = 0;

        for (int i = 0; i < DIM - 1; i++) {
            if (getField(i, i + 1) == m) {
                left++;
            }
        }
        if (left == 5) {
            correctDiagonal = true;
        }
        left = 0;

        for (int i = 0; i < DIM - 1; i++) {
            if (getField(i + 1, i) == m) {
                left++;
            }
        }
        if (left == 5) {
            correctDiagonal = true;
        }


        //checking for top right to bottom left (all the three ways)
        for (int i = 0; i < DIM; i++) {
            if (getField(i, (DIM - 1) - i) == m) {
                right++;
            }
        }
        if (right == 6 ||
                right == 5 && (getField(0, DIM - 1) != m || getField(DIM - 1, 0) != m)) {
            correctDiagonal = true;
        }
        right = 0;

        for (int i = 0; i < DIM - 1; i++) {
            if (getField(i, (DIM - 1) - (i + 1)) == m) {
                right++;
            }
        }
        if (right == 5) {
            correctDiagonal = true;
        }
        right = 0;

        for (int i = 0; i < DIM - 1; i++) {
            if (getField(i + 1, (DIM - 1) - i) == m) {
                right++;
            }
        }
        if (right == 5) {
            correctDiagonal = true;
        }

        return correctDiagonal;
    }


    /**
     * Checks if the mark m has won. A mark wins if it controls at
     * least one row, column or diagonal.
     *
     * @param m the mark of interest
     * @return true if the mark has won
     */
    /*@ requires m == Mark.BLACK || m == Mark.WHITE;
    ensures hasRow(m) || hasColumn(m) || hasDiagonal(m) ==> \result == true;
     @*/
    /*@ pure */public boolean isWinner(Mark m) {

        return hasRow(m) || hasColumn(m) || hasDiagonal(m);
    }

    /**
     * Checks if there is a draw, so if both players won at the same time, or if the board is full.
     *
     * @return true if there is a draw, false if there isn't
     */
    /*@
        ensures (isWinner(Mark.BLACK) && isWinner(Mark.WHITE)) || isFull() ==> \result == true;
    */
    /*@ pure */public boolean isDraw() {
        return isWinner(Mark.BLACK) && isWinner(Mark.WHITE) || isFull();
    }


    /**
     * Returns true if the game has a winner. This is the case when one of the
     * marks controls at least one row, column or diagonal.
     *
     * @return true if the student has a winner.
     */
    //@ ensures isWinner(Mark.BLACK) ^ isWinner(Mark.WHITE) ==> \result == true;
    /*@ pure */public boolean hasWinner() {
        return isWinner(Mark.WHITE) ^ isWinner(Mark.BLACK);
    }


    /**
     * Empties all fields of this board (i.e., let them refer to the value
     * Mark.EMPTY).
     */
    //@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); fields[i] == Mark.EMPTY);
    public void reset() {
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = Mark.EMPTY;
        }
    }


    /**
     * Sets the content of field i to the mark m.
     *
     * @param i the field number (see NUMBERING)
     * @param m the mark to be placed
     */
    /*@ requires isField(i);
    ensures getField(i) == m;
     @*/
    public void setField(int i, Mark m) {
        fields[i] = m;
    }


    /**
     * Sets the content of the field represented by the (row,col) pair to the
     * mark m.
     *
     * @param row the field's row
     * @param col the field's column
     * @param m   the mark to be placed
     */
    /*@ requires isField(row, col);
    ensures getField(row, col) == m;
     @*/
    public void setField(int row, int col, Mark m) {
        fields[index(row, col)] = m;
    }

    /**
     * Rotates the selected quadrant of the board.
     * <p>
     * literely trial-and-errored the shit out of this method, we first looked
     * at what rotating a quadrant did to the index numbers, and wrote
     * down how the every index in a quadrant
     * chances if we rotated it clock and counter clockwise, then we
     * discovered that the diference between the indexes of the
     * quadrants can be calculated with a so called multFactor
     * that looks at the relative position of the first index
     * of a quadrant (the topleft cel) those are, in order, 0-3-18-21
     *
     * @param command is the quadrants number
     */
    /*@
        requires command >= 0 && command <= 7;
    */
    public void rotateBoard(int command) {
        Board copyBoard = deepCopy();
        int[] clockwise = {12, 5, -2, 7, 0, -7, 2, -5, -12};
        int[] counterClockwise = {2, 7, 12, -5, 0, 5, -12, -7, -2};
        int[] using;
        int multFactor = 0;
        int direction = command % 2;     //0 = counter clockwise and 1 = clockwise
        int quadrant = command / 2;     //0 = top left, 1 =top right, 2=bottom left, 3=bottem right
        switch (quadrant) {
            case 0:
                multFactor = 0;
                break;
            case 1:
                multFactor = 3;
                break;
            case 2:
                multFactor = 18;
                break;
            case 3:
                multFactor = 21;
                break;
        }

        if (direction == 0) {
            using = counterClockwise;
        } else {
            using = clockwise;
        }

        //copying the value from a copy of the board.
        fields[multFactor] = copyBoard.fields[multFactor + using[0]];
        fields[1 + multFactor] = copyBoard.fields[1 + multFactor + using[1]];
        fields[2 + multFactor] = copyBoard.fields[2 + multFactor + using[2]];
        fields[6 + multFactor] = copyBoard.fields[6 + multFactor + using[3]];
        fields[7 + multFactor] = copyBoard.fields[7 + multFactor + using[4]];
        fields[8 + multFactor] = copyBoard.fields[8 + multFactor + using[5]];
        fields[12 + multFactor] = copyBoard.fields[12 + multFactor + using[6]];
        fields[13 + multFactor] = copyBoard.fields[13 + multFactor + using[7]];
        fields[14 + multFactor] = copyBoard.fields[14 + multFactor + using[8]];

    }

    /**
     * The method returns the game board.
     *
     * @return String value of the board.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < DIM; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < DIM; j++) {
                row.append(" ").append(getField(i, j).toString().substring(0, 1).replace("E", " ")).append(" ");
                if (j == DIM / 2 - 1) {
                    row.append("||");
                } else if (j < DIM - 1) {
                    row.append("|");
                }
            }
            s.append(row).append(SPACE).append(NUMBERING[i * 2]);
            if (i == DIM / 2 - 1) {
                s.append("\n").append(BIGLINE).append(SPACE).append(NUMBERING[i * 2 + 1]).append("\n");
            } else if (i < DIM - 1) {
                s.append("\n").append(SMALLLINE).append(SPACE).append(NUMBERING[i * 2 + 1]).append("\n");
            }

        }
        return s.toString();
    }

}
