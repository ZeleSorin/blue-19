package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestIndex {
    Board board = new Board();
    Mark m1 = Mark.BLACK;
    Mark m2 = Mark.WHITE;

    @Test
    public void testIndex() {
        int index = board.index(2, 5);
        assertEquals(17, index);

        index = board.index(4, 5);
        assertEquals(29, index);

        index = board.index(3, 3);
        assertEquals(21, index);
    }
}
