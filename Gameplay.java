import java.util.Scanner;

public class GamePlay {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hosts host = new Hosts("Game", "Master");
        Players[] currentPlayers = new Players[3];

        // Player setup loop.
        for (int i = 0; i < currentPlayers.length; i++) {
            System.out.print("Enter first name for Player " + (i + 1) + ": ");
            String firstName = scanner.nextLine();
            currentPlayers[i] = new Players(firstName);
            System.out.println("Welcome, " + currentPlayers[i].getFirstName() + "!");
        }

        // Main Game Loop 
        while (true) {
            System.out.println("\n--- Let's Start A New Round! ---");
            host.setNewPhrase(); // Host sets a new phrase for the round.

            int currentPlayerIndex = 0;
            boolean isMoneyRound = true; // Prize type can alternate per round.
            boolean phraseGuessed = false;

            // Round Loop
            while (!phraseGuessed) {
                Players currentPlayer = currentPlayers[currentPlayerIndex];

                // Player can only play if they have money.
                if (currentPlayer.getMoney() <= 0) {
                    System.out.println("\n" + currentPlayer.getFirstName() + " is out of money and must sit out.");
                } else {
                    // A single player takes their turn. The result determines if the round ends.
                    phraseGuessed = new Turn().takeTurn(currentPlayer, host, scanner, isMoneyRound);
                }
                
                // If phrase is guessed, the inner round loop will terminate.
                if (phraseGuessed) {
                    break;
                }

                // Move to the next player for the next turn.
                currentPlayerIndex = (currentPlayerIndex + 1) % currentPlayers.length;

                // Alternate the prize type after a full cycle of players.
                if (currentPlayerIndex == 0) {
                    isMoneyRound = !isMoneyRound;
                    System.out.println("\n--- Prize Type Switched! ---");
                }
            }

            // After a phrase is guessed, ask to play another round.
            System.out.println("\n--- Round Over ---");
            System.out.print("Would you like to play another round? (yes/no): ");
            String playAgainResponse = scanner.nextLine();

            if (!playAgainResponse.equalsIgnoreCase("yes")) {
                break; // Exit the main game loop.
            }
        }

        // Display final standings at the end of the game.
        System.out.println("\n--- Thank you for playing! ---");
        System.out.println("Final Standings:");
        for (Players p : currentPlayers) {
            System.out.println(p.toString());
        }
        scanner.close();
    }
}