package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoardIsFull {
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    public void testFullBoard() {
        Mark m = Mark.WHITE;
        for (int i = 0; i < board.fields.length; i++) {
            board.setField(i, m);
        }
        for (int i = 0; i < board.fields.length; i++) {
            assertTrue(board.getField(i) == Mark.WHITE);
            assertFalse(board.isEmptyField(i));
        }
        assertTrue(board.hasWinner());
        assertTrue(board.gameOver());
        assertTrue(board.isFull());
    }
}
