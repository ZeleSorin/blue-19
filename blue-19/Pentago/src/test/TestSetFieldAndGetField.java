package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSetFieldAndGetField {
    private Board board;
    private Mark mark1;
    private Mark mark2;

    @BeforeEach
    public void setup() {
        this.board = new Board();
        this.mark1 = Mark.BLACK;
        this.mark2 = Mark.WHITE;
    }

    @Test

    public void testSetField() {
        // set a field with a specified Mark
        board.setField(2, mark1);
        assertTrue(board.isField(2));

        board.setField(14, mark2);
        assertTrue(board.isField(14));

        board.setField(35, mark2);
        assertTrue(board.isField(35));


    }

    @Test
    public void testGetField() {

        board.setField(2, mark1);
        assertEquals(board.getField(2), mark1);

        board.setField(35, mark2);
        assertEquals(board.getField(35), mark2);
    }

}
