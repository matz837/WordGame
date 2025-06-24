import java.util.Scanner;

public class GamePlay {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Create an instance of the Host class.
        Hosts host = new Hosts("Game", "Master");

        // 2. Prompt for player's name and create a Players instance.
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Would you like to enter a last name? (yes/no): ");
        String response = scanner.nextLine();

        Players player;
        if (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();
            player = new Players(firstName, lastName);
        } else {
            player = new Players(firstName);
        }
        System.out.println("Welcome, " + player.getFirstName() + "!");
        System.out.println(player.toString());

        // 3. Create an instance of the Turn class.
        Turn turn = new Turn();
        String playAgainResponse;

        // 4. Outer loop to allow the player to play multiple games.
        do {
            // Check if the player has money to play.
            if (player.getMoney() <= 0) {
                System.out.println("You don't have any money left to play. Game over!");
                break;
            }

            host.randomizeNum();

            boolean guessedCorrectly = false;
            // 5. Inner loop for guessing, calls takeTurn() until it returns true or player runs out of money.
            while (!guessedCorrectly && player.getMoney() > 0) {
                guessedCorrectly = turn.takeTurn(player, host, scanner);
            }

            if (player.getMoney() <= 0) {
                System.out.println("\n--- You've run out of money! ---");
                break; // Exit the outer loop if money is gone
            }
            
            System.out.println("\n--- Round Over ---");
            System.out.print("Would you like to play another round? (yes/no): ");
            playAgainResponse = scanner.nextLine();

        } while (playAgainResponse.equalsIgnoreCase("yes"));

        System.out.println("\nThank you for playing! Final Status: " + player.toString());
        scanner.close();
    }
}
