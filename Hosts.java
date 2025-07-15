import java.util.Random;

public class Hosts extends Players {
    // A list of possible phrases for the game.
    private static final String[] PHRASE_LIST = {
        "Wheel of Fortune",
        "A piece of cake",
        "Once in a blue moon",
        "The early bird gets the worm",
        "You just won a pirze"
    };

    public Hosts(String firstName, String lastName) {
        super(firstName, lastName);
        this.setMoney(0);
    }

    // Selects a new random phrase and sets it in the Phrases class for the round.
    public void setNewPhrase() {
        System.out.println("\n" + getFirstName() + " is thinking of a new phrase...");
        Random rand = new Random();
        String chosenPhrase = PHRASE_LIST[rand.nextInt(PHRASE_LIST.length)];
        // Use the Phrases class to set up the chosen phrase.
        Phrases.setGamePhrase(chosenPhrase);
    }
}