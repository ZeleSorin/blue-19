package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestBoardWinConditions {
    private Board board;

    @BeforeEach
    public void setup() {
        this.board = new Board();
    }

    @Test
    public void testRow() {
        Mark m = Mark.BLACK;
        for (int i = 0; i < 6; i++) {
            board.setField(0 + i * 6, m);
            assertFalse(board.hasWinner());
            board.setField(1 + i * 6, m);
            assertFalse(board.hasWinner());
            board.setField(2 + i * 6, m);
            assertFalse(board.hasWinner());
            board.setField(3 + i * 6, m);
            assertFalse(board.hasWinner());
            board.setField(4 + i * 6, m);
            assertTrue(board.hasWinner());
            board.setField(5 + i * 6, m);
            assertTrue(board.hasWinner());
            board.setField(0 + i * 6, m.other());
            assertTrue(board.hasWinner());
            board.setField(1 + i * 6, m.other());
            assertFalse(board.hasWinner());

            board.reset();
            assertFalse(board.hasWinner());
            System.out.println("the wincondition on row: " + i + " works");
        }

    }

    @Test
    public void testColumn() {
        Mark m = Mark.WHITE;
        for (int i = 0; i < 6; i++) {
            board.setField(0 + i, m);
            assertFalse(board.hasWinner());
            board.setField(6 + i, m);
            assertFalse(board.hasWinner());
            board.setField(12 + i, m);
            assertFalse(board.hasWinner());
            board.setField(18 + i, m);
            assertFalse(board.hasWinner());
            board.setField(24 + i, m);
            assertTrue(board.hasWinner());
            board.setField(30 + i, m);
            assertTrue(board.hasWinner());
            board.setField(0 + i, Mark.EMPTY);
            assertTrue(board.hasWinner());
            board.setField(6 + i, Mark.EMPTY);
            assertFalse(board.hasWinner());

            board.reset();
            assertFalse(board.hasWinner());
            System.out.println("the wincondition on column: " + i + " works");
        }

    }

    // testing the wincondition on the diagonal lines next to the main diagonal
    // lines starting in the topleft quadrant.
    // (1-8-15-22-29),(6-13-20-27-34), (4-9-12-19-24) and (11-16-21-26-31)
    @Test
    public void testDiagonalShort() {
        Mark m = Mark.BLACK;

        // if the indexes of one of these arrays all have the same mark,
        // the player with that mark has wincondition
        int[] left1 = {1, 8, 15, 22, 29};
        int[] left2 = {6, 13, 20, 27, 34};
        int[] right1 = {4, 9, 14, 19, 24};
        int[] right2 = {11, 16, 21, 26, 31};
        List<int[]> shortDiagonals = new ArrayList<>();

        shortDiagonals.add(left1);
        shortDiagonals.add(left2);
        shortDiagonals.add(right1);
        shortDiagonals.add(right2);

        for (int[] a: shortDiagonals) {
            board.setField(a[0], m);
            assertFalse(board.hasWinner());
            board.setField(a[1], m);
            assertFalse(board.hasWinner());
            board.setField(a[2], m);
            assertFalse(board.hasWinner());
            board.setField(a[3], m);
            assertFalse(board.hasWinner());
            board.setField(a[4], m);
            assertTrue(board.hasWinner());
            board.setField(a[0], Mark.EMPTY);
            assertFalse(board.hasWinner());
            board.setField(a[0], m.other());
            assertFalse(board.hasWinner());
            board.reset();
            assertFalse(board.hasWinner());
        }
    }

    @Test
    void testDiagonalLong() {
        Mark m = Mark.WHITE;

        //first checking from topleft to bottomright
        for (int i = 0; i < 4; i++) {
            board.setField(i, i, m);
            assertFalse(board.hasWinner());
        }
        board.setField(4, 4, m);
        assertTrue(board.hasWinner());
        board.setField(5, 5, m);
        assertTrue(board.hasWinner());
        board.setField(4, 4, m.other());
        assertFalse(board.hasWinner());

        board.reset();

        //now checking from topright to bottomleft
        for (int i = 0; i < 4; i++) {
            board.setField(i, 5 - i, m);
            assertFalse(board.hasWinner());
        }
        board.setField(4, 1, m);
        assertTrue(board.hasWinner());
        board.setField(5, 0, m);
        assertTrue(board.hasWinner());
        board.setField(4, 1, m.other());
        assertFalse(board.hasWinner());


    }

}
