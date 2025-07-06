import java.util.Scanner;

public class Turn {

    public boolean takeTurn(Players player, Hosts host, Scanner scanner, boolean isMoneyRound) {
        Award currentAward;
        String prizeMessage;

        // The prize type is determined by the isMoneyRound parameter.
        if (isMoneyRound) {
            currentAward = new Money();
            prizeMessage = "This round is for cash!";
        } else {
            currentAward = new Physical();
            prizeMessage = "This round is for a fabulous physical prize!";
        }

        System.out.println("\n" + host.getFirstName() + " says: \"" + prizeMessage + "\"");
        System.out.print(player.getFirstName() + ", please enter a guess: ");
        
        int guess = scanner.nextInt();
        scanner.nextLine(); 

        boolean isCorrect = Numbers.compareNumber(guess);
        
        int moneyChange = currentAward.displayWinnings(player, isCorrect);
        player.setMoney(player.getMoney() + moneyChange);

        if (player.getMoney() < 0) {
            player.setMoney(0);
        }

        if (isCorrect) {
            System.out.println("That's it! You guessed the number!");
        } else {
             if (guess > Numbers.getRandomNum()) {
                System.out.println("I'm sorry. That guess was too high.");
            } else {
                System.out.println("I'm sorry. That guess was too low.");
            }
        }
        
        System.out.println(player.toString()); 
        return isCorrect;
    }
}