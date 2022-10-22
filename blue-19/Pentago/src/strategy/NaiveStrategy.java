package strategy;

import board.Board;
import mark.Mark;

import java.util.ArrayList;
import java.util.List;

public class NaiveStrategy implements Strategy {

    //This method return the name of the Strategy
    @Override
    public String getName() {
        return "Naive";
    }

    //This method returns a random move;
    @Override
    public int determineMove(Board board, Mark mark) {
        List<Integer> emptyFields = new ArrayList<>();
        for (int i = 0; i < board.fields.length; i++) {
            if (board.isEmptyField(i)) {
                emptyFields.add(i);
            }
        }

        return emptyFields.get((int) (Math.random() * (emptyFields.size() - 1)));
    }

    //returns a value that rotates a random quadrant in a random direction.
    @Override
    public int determineRotation(Board board, Mark mark) {
        int toRotate = (int) (Math.random() * 7);
        return toRotate;
    }

}
