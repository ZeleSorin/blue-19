package test;

import board.Board;
import mark.Mark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRotateBoard {
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }
    @Test
    public void testrotation() {
        board.setField(0, Mark.WHITE);
        board.setField(5, Mark.WHITE);
        board.setField(30, Mark.WHITE);
        board.setField(35, Mark.WHITE);
        System.out.println("This is the Board with an element in each corner");
        System.out.println(board.toString());
        board.rotateBoard(1);
        board.rotateBoard(1);
        board.rotateBoard(4);
        board.rotateBoard(4);
        board.rotateBoard(3);
        board.rotateBoard(3);
        board.rotateBoard(7);
        board.rotateBoard(7);
        System.out.println("This is the board with the elements " +
                "rotated to the middle of the table");
        System.out.println(board.toString() + "\n \n \n");
    }

}
