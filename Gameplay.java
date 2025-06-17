import java.util.Scanner;

public class GamePlay {
    private static Person player;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt for first name
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        // Ask if user wants to enter a last name
        System.out.print("Would you like to enter a last name? (yes/no): ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();
            player = new Person(firstName, lastName);
        } else {
            player = new Person(firstName);
        }

        // Instantiate Numbers and generate number
        Numbers numbers = new Numbers();
        numbers.generateNumber();

        // Guess loop
        boolean guessedCorrectly = false;
        while (!guessedCorrectly) {
            System.out.print(player.getFirstName() + ", enter a guess between 0 and 100: ");
            int guess = scanner.nextInt();
            guessedCorrectly = numbers.compareNumber(guess);
        }

        scanner.close();
    }
}
