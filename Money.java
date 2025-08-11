public class Money implements Award {

    private static final int WINNING_AMOUNT = 500;
    private static final int INCORRECT_GUESS_PENALTY = 50;

    @Override
    public int displayWinnings(Players player, boolean wasCorrect) {
        // Logic now only returns the value; GUI handles the display message.
        if (wasCorrect) {
            return WINNING_AMOUNT;
        } else {
            return -INCORRECT_GUESS_PENALTY;
        }
    }
}