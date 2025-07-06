public class Money implements Award {

    private static final int WINNING_AMOUNT = 500;
    private static final int INCORRECT_GUESS_PENALTY = 50;

    @Override
    public int displayWinnings(Players player, boolean wasCorrect) {
        if (wasCorrect) {
            System.out.println(player.getFirstName() + " won $" + WINNING_AMOUNT + "!");
            return WINNING_AMOUNT;
        } else {
            System.out.println(player.getFirstName() + " lost $" + INCORRECT_GUESS_PENALTY + ".");
            // Return a negative value for the penalty.
            return -INCORRECT_GUESS_PENALTY;
        }
    }
}
