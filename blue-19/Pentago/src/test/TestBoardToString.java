package test;

import board.Board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBoardToString {
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    public void testBoardToString() {
        System.out.println(board.toString());
    }


}
