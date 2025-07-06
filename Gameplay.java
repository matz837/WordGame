import java.util.Scanner;

public class GamePlay {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hosts host = new Hosts("Game", "Master");
        Players[] currentPlayers = new Players[3];

        for (int i = 0; i < currentPlayers.length; i++) {
            System.out.print("Enter first name for Player " + (i + 1) + ": ");
            String firstName = scanner.nextLine();
            currentPlayers[i] = new Players(firstName);
            System.out.println("Welcome, " + currentPlayers[i].getFirstName() + "!");
        }

        System.out.println("\n--- Let's Start The Game! ---");
        for (Players p : currentPlayers) {
            System.out.println(p.toString());
        }

        Turn turn = new Turn();
        int currentPlayerIndex = 0;
        
        // This boolean now controls the prize type for the entire round of players.
        // true = Money, false = Physical. It starts with Money.
        boolean isMoneyRound = true;

        host.randomizeNum();

        while (true) {
            Players currentPlayer = currentPlayers[currentPlayerIndex];

            if (currentPlayer.getMoney() <= 0) {
                System.out.println("\n" + currentPlayer.getFirstName() + " is out of money and cannot play this round.");
                
            } else {
                 // Pass the current round's prize type (isMoneyRound) to the takeTurn method.
                boolean guessedCorrectly = turn.takeTurn(currentPlayer, host, scanner, isMoneyRound);
    
                if (guessedCorrectly) {
                    System.out.println("\n--- Round Over ---");
                    System.out.print("Would you like to play another round? (yes/no): ");
                    String playAgainResponse = scanner.nextLine();
    
                    if (playAgainResponse.equalsIgnoreCase("yes")) {
                        host.randomizeNum();
                    } else {
                        break;
                    }
                }
            }
            
            // Move to the next player for the next turn.
            currentPlayerIndex = (currentPlayerIndex + 1) % currentPlayers.length;

            // If the index has reset to 0, it means a full cycle of players is complete and the prize type is changed
            if (currentPlayerIndex == 0) {
                isMoneyRound = !isMoneyRound;
            }
        }

        System.out.println("\n--- Thank you for playing! ---");
        System.out.println("Final Standings:");
        for (Players p : currentPlayers) {
            System.out.println(p.toString());
        }
        scanner.close();
    }
}