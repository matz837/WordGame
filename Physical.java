import java.util.Random;

public class Physical implements Award {

    // An array of at least 5 physical prizes.
    private final String[] prizes = {
        "a Brand New Car",
        "a New Macbook Pro",
        "a Trip to Hawaii",
        "a Lifetime Supply of Cheese",
        "a Complete Home Makeover"
    };

    //Gets a random prize from the prizes array.
    private int getRandomPrize() {
        Random rand = new Random();
        return rand.nextInt(prizes.length);
    }

    @Override
    public int displayWinnings(Players player, boolean wasCorrect) {
        int prizeIndex = getRandomPrize();
        String prize = prizes[prizeIndex];

        if (wasCorrect) {
            System.out.println(player.getFirstName() + " won " + prize + "!");
        } else {
            System.out.println("Tough luck, " + player.getFirstName() + ". You could have won " + prize + ".");
        }
        
        // Physical prizes do not affect the player's money.
        return 0;
    }
}