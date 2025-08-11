import java.util.Random;

public class Physical implements Award {

    // An array of physical prizes.
    private final String[] prizes = {
        "a Brand New Car",
        "a New Macbook Pro",
        "a Trip to Hawaii",
        "a Lifetime Supply of Cheese",
        "a Complete Home Makeover"
    };

    //Gets a random prize from the prizes array.
    private String getRandomPrize() {
        Random rand = new Random();
        return prizes[rand.nextInt(prizes.length)];
    }

    //This method is part of the interface but no longer displays anything.
    //It simply returns 0 as physical prizes do not affect money.
    @Override
    public int displayWinnings(Players player, boolean wasCorrect) {
        // Physical prizes do not affect the player's money.
        return 0;
    }

    //Gets a formatted string describing the prize outcome for the GUI.
    public String getPrizeForDisplay(boolean wasCorrect) {
        String prize = getRandomPrize();
        if (wasCorrect) {
            return "won " + prize + "!";
        } else {
            return "could have won " + prize + ".";
        }
    }
}