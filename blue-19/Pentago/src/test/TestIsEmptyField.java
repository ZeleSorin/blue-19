package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestIsEmptyField {
    Board board = new Board();
    Mark m1 = Mark.BLACK;
    Mark m2 = Mark.WHITE;

    @Test
    public void testIsEmptyField() {
        int checker = 0;
        // first we fill the board
        for (int i = 0; i < 35; i++) {
            board.setField(i, m1);
        }

        // now we check that all the places are full;
        for (int i = 0; i < 35; i++) {
            if (board.isEmptyField(i)) {
                checker++;
            }
        }
        //at the end we check if any field was empty, If checker is > 0,
        // our method is not working propperly
        assertEquals(checker, 0);

    }
}
