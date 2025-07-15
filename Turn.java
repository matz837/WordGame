import java.util.Scanner;

public class Turn {

    public boolean takeTurn(Players player, Hosts host, Scanner scanner, boolean isMoneyRound) {
        Award currentAward;
        String prizeMessage;

        // Determine the prize type for the round.
        if (isMoneyRound) {
            currentAward = new Money();
            prizeMessage = "This round is for cash!";
        } else {
            currentAward = new Physical();
            prizeMessage = "This round is for a fabulous physical prize!";
        }

        System.out.println("\n" + host.getFirstName() + " says: \"" + prizeMessage + "\"");
        System.out.println("The current phrase is: " + Phrases.getPlayingPhrase());
        System.out.print(player.getFirstName() + ", please guess a letter: ");
        
        boolean letterFound = false;

        try {
            String guess = scanner.nextLine().trim();
            // Validate input: must be a single letter.
            if (guess.length() != 1) {
                throw new MultipleLettersException();
            }
            if (!Character.isLetter(guess.charAt(0))) {
                System.out.println("Invalid input. Please enter a single letter.");
                return false; // End the turn on invalid input.
            }

            // Process the letter guess.
            letterFound = Phrases.findLetters(guess);

        } catch (MultipleLettersException e) {
            System.out.println("Error: " + e.getMessage());
            letterFound = false; // Treat as an incorrect guess.
        }

        // Apply winnings or penalties based on the guess.
        int moneyChange = currentAward.displayWinnings(player, letterFound);
        player.setMoney(player.getMoney() + moneyChange);
        if (player.getMoney() < 0) {
            player.setMoney(0);
        }

        if (letterFound) {
            System.out.println("Good guess! That letter is in the phrase.");
        } else {
            System.out.println("I'm sorry, that letter is not in the phrase.");
        }
        
        System.out.println("The phrase is now: " + Phrases.getPlayingPhrase());
        System.out.println(player.toString()); 
        
        // Check if the entire phrase has been guessed.
        if (Phrases.isPhraseGuessed()) {
            System.out.println("\nCongratulations " + player.getFirstName() + "! You solved the phrase!");
            return true; // Return true to signify the round is over.
        }

        return false; // Return false to continue the round.
    }
}