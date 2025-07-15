public class Phrases {
    // Stores the complete, hidden game phrase.
    private static String gamePhrase;
    // Stores the visible phrase with underscores for unguessed letters.
    private static String playingPhrase;

    // Getters 
    public static String getPlayingPhrase() {
        return playingPhrase;
    }

    //Sets a new game phrase and generates the initial playing phrase with underscores.
    public static void setGamePhrase(String phrase) {
        gamePhrase = phrase.toUpperCase(); // Store in upper case for case-insensitive comparison
        StringBuilder sb = new StringBuilder();
        for (char c : gamePhrase.toCharArray()) {
            // Replace letters with underscores; preserve spaces and other characters.
            if (Character.isLetter(c)) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        playingPhrase = sb.toString();
    }

    //Processes a player's letter guess and updates the playing phrase.
    public static boolean findLetters(String guess) throws MultipleLettersException {
        // Ensure the guess is a single character.
        if (guess.length() != 1) {
            throw new MultipleLettersException();
        }

        char guessedLetter = guess.toUpperCase().charAt(0);
        boolean letterFound = false;
        StringBuilder newPlayingPhrase = new StringBuilder(playingPhrase);

        // Find all occurrences of the guessed letter.
        for (int i = 0; i < gamePhrase.length(); i++) {
            if (gamePhrase.charAt(i) == guessedLetter) {
                newPlayingPhrase.setCharAt(i, guessedLetter);
                letterFound = true;
            }
        }

        playingPhrase = newPlayingPhrase.toString();
        return letterFound;
    }
    
    //Checks if the entire phrase has been successfully guessed.
    public static boolean isPhraseGuessed() {
        return !playingPhrase.contains("_");
    }
}