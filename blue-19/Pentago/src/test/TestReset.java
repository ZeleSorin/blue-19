package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TestReset {
    private Board board;
    private Mark m1 = Mark.BLACK;

    @BeforeEach
    public void setup() {
        this.board = new Board();
    }

    @Test
    public void testReset() {
        int random;

        for (int i = 0; i < 1000; i++) {
            random = (int) (Math.random() * 35 + 1);
            board.setField(random, m1);
            board.reset();
            assertTrue(board.isEmptyField(random));
            System.out.println("Test passed for random number " + random);


        }
    }

}
