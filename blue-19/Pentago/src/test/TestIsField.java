package test;

import board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIsField {

    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    void testIsField() {
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            int field = rand.nextInt(101);
            System.out.println(field);
            if (field <= 35) {
                assertTrue(board.isField(field));
            } else {
                assertFalse(board.isField(field));
            }
        }

        for (int i = 0; i < 50; i++) {
            int column = rand.nextInt(11);
            int row = rand.nextInt(11);
            System.out.println(column + ", " + row);
            if (column <= 5 && row <= 5) {
                assertTrue(board.isField(row, column));
            } else {
                assertFalse(board.isField(row, column));
            }
        }

    }
}
