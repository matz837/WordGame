import java.util.Random;

public class Numbers {
    // randomNum is static so it's shared and persists without an instance.
    private static int randomNum;

    public static int getRandomNum() {
        return randomNum;
    }

    public static void setRandomNum(int newRandomNum) {
        randomNum = newRandomNum;
    }

    public static void generateNumber() {
        Random rand = new Random();
        randomNum = rand.nextInt(101); // Generates a number from 0-100
    }

    // compareNumber is static and only returns true or false.
    public static boolean compareNumber(int guess) {
        return guess == randomNum;
    }
}