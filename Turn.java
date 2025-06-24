import java.util.Scanner;

public class Turn {

    private static final int WINNING_AMOUNT = 500;
    private static final int INCORRECT_GUESS_PENALTY = 50;

    // The single scanner from GamePlay is passed in, not created here.
    public boolean takeTurn(Players player, Hosts host, Scanner scanner) {
        System.out.print(host.getFirstName() + " says: \"" + player.getFirstName() + ", please enter a guess\": ");
        
        int guess = scanner.nextInt();
        scanner.nextLine(); 

        // Call the static method directly from the Numbers class.
        boolean isCorrect = Numbers.compareNumber(guess);

        if (isCorrect) {
            // Player guessed correctly.
            System.out.println("That's it! You guessed the number!");
            player.setMoney(player.getMoney() + WINNING_AMOUNT);
            System.out.println("Congratulations! You won $" + WINNING_AMOUNT);
            System.out.println(player.toString()); 
            return true;
        } else {
            // Player guessed incorrectly.
            if (guess > Numbers.getRandomNum()) {
                System.out.println("I'm sorry. That guess was too high.");
            } else {
                System.out.println("I'm sorry. That guess was too low.");
            }
            
            // Prevent money from going negative.
            int currentMoney = player.getMoney();
            int newMoney = Math.max(0, currentMoney - INCORRECT_GUESS_PENALTY);
            player.setMoney(newMoney);

            System.out.println("That was not the number. You lose $" + INCORRECT_GUESS_PENALTY);
            System.out.println(player.toString()); 
            return false;
        }
    }
}