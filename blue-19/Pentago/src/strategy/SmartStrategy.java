package strategy;

import board.Board;
import mark.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartStrategy implements Strategy {
    @Override
    public String getName() {
        return "SmartAI";
    }

    @Override
    public int determineMove(Board board, Mark mark) {
        Random rand = new Random();

        //check if there are any winning moves
        for (int i = 0; i < board.fields.length; i++) {
            Board copy = board.deepCopy();
            if (copy.isEmptyField(i)) {
                copy.setField(i, mark);
                for (int j = 0; j < 7; j++) {
                    Board copyCopy = copy.deepCopy();
                    copyCopy.rotateBoard(j);
                    if (copyCopy.isWinner(mark)) {
                        return i;
                    }
                }
            }
        }

        //check if the opponent has any winning moves
        for (int i = 0; i < board.fields.length; i++) {
            Board copy = board.deepCopy();
            if (copy.isEmptyField(i)) {
                copy.setField(i, mark.other());
                for (int j = 0; j < 7; j++) {
                    Board copyCopy = copy.deepCopy();
                    copyCopy.rotateBoard(j);
                    if (copyCopy.isWinner(mark.other())) {
                        return i;
                    }
                }
            }
        }

        //check if there are more then 3 of your mark in a row
        for (int i = 0; i < 6; i++) {
            List<Integer> emptyFieldsRow = new ArrayList<>();
            int row = 0;
            for (int j = 0; j < 6; j++) {
                if (board.getField(i, j).equals(mark)) {
                    row++;
                    if (board.isEmptyField(i, j)) {
                        emptyFieldsRow.add(board.index(i, j));
                    }
                }
            }
            if (row >= 3 && emptyFieldsRow.size() > 0) {
                return emptyFieldsRow.get(rand.nextInt(emptyFieldsRow.size()));
            }
        }

        //check if there are more then 3 of your mark in a column
        for (int i = 0; i < 6; i++) {
            List<Integer> emptyFieldsColumn = new ArrayList<>();
            int column = 0;
            for (int j = 0; j < 6; j++) {
                if (board.getField(j, i).equals(mark)) {
                    column++;
                    if (board.isEmptyField(j, i)) {
                        emptyFieldsColumn.add(board.index(j, i));
                    }
                }
            }
            if (column >= 3 && emptyFieldsColumn.size() > 0) {
                return emptyFieldsColumn.get(
                        rand.nextInt(emptyFieldsColumn.size()));
            }
        }

        //choose a random field in the middle spots if they are empty (7, 10, 25, 28)
        List<Integer> emptyFieldsMid = new ArrayList<>();
        int[] midFields = {7, 10, 25, 28};
        for (int mid: midFields) {
            if (board.isEmptyField(mid)) {
                emptyFieldsMid.add(mid);
            }
        }
        if (emptyFieldsMid.size() > 0) {
            return emptyFieldsMid.get(rand.nextInt(emptyFieldsMid.size()));
        }


        //just choose a random field if there are no win conditions
        List<Integer> emptyFields = new ArrayList<>();
        for (int i = 0; i < board.fields.length; i++) {
            if (board.isEmptyField(i)) {
                emptyFields.add(i);
            }
        }

        return emptyFields.get(rand.nextInt(emptyFields.size()));
    }

    @Override
    public int determineRotation(Board board, Mark mark) {
        for (int i = 0; i < 7; i++) {
            Board copy = board.deepCopy();
            copy.rotateBoard(i);
            if (copy.isWinner(mark)) {
                return i;
            }
        }

        for (int i = 0; i < 7; i++) {
            Board copy = board.deepCopy();
            copy.rotateBoard(i);
            if (copy.isWinner(mark.other())) {
                return i;
            }
        }

        return (int) (Math.random() * 7);
    }
}
