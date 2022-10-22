package mark;

/**
 * represents the three possible values of a field on the board:
 * Mark.BLACK, Mark.WHITE, Mark.EMPTY.
 */
public enum Mark {
    BLACK,
    WHITE,
    EMPTY;

    /**
     * returns the opposite mark.
     * @return the opposite mark of a selected Mark, or EMPTY if the selected mark is EMPTY.
     */
    public Mark other() {
        if (this == BLACK) {
            return WHITE;
        } else if (this == WHITE) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }

}
