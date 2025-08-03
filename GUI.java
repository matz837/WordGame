import javax.swing.*;
import java.awt.FlowLayout;
import java.util.ArrayList;

//Handles the Graphical User Interface and the main game flow for the word game.
public class GUI extends JFrame {

    // --- Game Data ---
    private final ArrayList<Players> playerList;
    private Hosts host;
    private int currentPlayerIndex;
    private boolean isMoneyRound;

    // --- GUI Components ---
    private final JLabel playersLabel;
    private final JLabel hostLabel;
    private final JLabel phraseLabel;
    private final JLabel turnLabel;
    private final JButton addPlayerButton;
    private final JButton setPhraseButton;
    private final JButton guessButton;

    public GUI() {
        // --- Initialize Game Data ---
        this.playerList = new ArrayList<>();
        this.host = new Hosts("Game", "Master"); // Default host
        this.currentPlayerIndex = 0;
        this.isMoneyRound = true;

        // --- Frame Setup ---
        setTitle("Word Game");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Simple layout

        // --- Component Initialization & Listeners ---
        this.turnLabel = new JLabel("Setup the game to begin!");
        this.hostLabel = new JLabel("Host: " + host.getFirstName());
        this.playersLabel = new JLabel("Players: No players have been added yet.");
        this.phraseLabel = new JLabel("Phrase: [Click 'Set Host & Phrase' to start]");
        
        this.addPlayerButton = new JButton("Add Player");
        addPlayerButton.addActionListener(e -> addPlayer());

        this.setPhraseButton = new JButton("Set Host & Phrase");
        setPhraseButton.addActionListener(e -> setHostAndPhrase());

        this.guessButton = new JButton("Guess a Letter");
        guessButton.addActionListener(e -> takeTurn());
        guessButton.setEnabled(false); // Disabled until game is ready

        // --- Add Components to Frame ---
        add(turnLabel);
        add(hostLabel);
        add(phraseLabel);
        add(playersLabel);
        add(addPlayerButton);
        add(setPhraseButton);
        add(guessButton);

        // Make the frame visible
        setVisible(true);
    }
    
    //Prompts for a new player's name and adds them to the game.
    private void addPlayer() {
        String name = JOptionPane.showInputDialog(this, "Enter player's name:", "Add Player", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            playerList.add(new Players(name.trim()));
            updatePlayersLabel();
            checkGameState();
        }
    }

    //Prompts for the host's name and changed so that the host sets the secret phrase for the round.
    private void setHostAndPhrase() {
        String hostName = JOptionPane.showInputDialog(this, "Enter host's name:", "Set Host", JOptionPane.PLAIN_MESSAGE);
        if (hostName != null && !hostName.trim().isEmpty()) {
            this.host = new Hosts(hostName.trim(), "");
            hostLabel.setText("Host: " + host.getFirstName());
        }

        String gamePhrase = JOptionPane.showInputDialog(this, "Enter the phrase for the game:", "Set Phrase", JOptionPane.PLAIN_MESSAGE);
        if (gamePhrase != null && !gamePhrase.trim().isEmpty()) {
            Phrases.setGamePhrase(gamePhrase);
            updatePhraseLabel();
            checkGameState();
        }
    }
    
    //Executes a single player's turn.
    private void takeTurn() {
        Players currentPlayer = playerList.get(currentPlayerIndex);

        // Skips the turn if the player is out of money.
        if (currentPlayer.getMoney() <= 0) {
            JOptionPane.showMessageDialog(this, currentPlayer.getFirstName() + " is out of money and must skip a turn.", "Out of Money", JOptionPane.INFORMATION_MESSAGE);
            nextPlayer();
            return;
        }

        // Prompts the player to guess a letter.
        String guess = JOptionPane.showInputDialog(this, currentPlayer.getFirstName() + ", please guess a letter:", "Guess a Letter", JOptionPane.PLAIN_MESSAGE);
        if (guess == null) return; // Player cancelled the dialog.

        boolean letterFound;
        String turnSummary;
        int moneyChange;

        // Processes the guess and handles exceptions.
        try {
            letterFound = Phrases.findLetters(guess.trim());
        } catch (MultipleLettersException ex) {
            letterFound = false; // Treat as an incorrect guess.
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }

        // Determine award, replacing console output with dialog messages.
        Award award = isMoneyRound ? new Money() : new Physical();
        moneyChange = award.displayWinnings(new Players(""), letterFound); // Use a dummy player to get the value without printing.
        
        if (letterFound) {
            turnSummary = "Good guess! That letter is in the phrase.";
        } else {
            turnSummary = "Sorry, that letter isn't in the phrase.";
        }

        currentPlayer.setMoney(currentPlayer.getMoney() + moneyChange);
        if (currentPlayer.getMoney() < 0) {
            currentPlayer.setMoney(0);
        }

        // Show a summary of the turn's result in a dialog.
        JOptionPane.showMessageDialog(this, turnSummary + "\n" + currentPlayer.toString(), "Turn Result", JOptionPane.INFORMATION_MESSAGE);

        updatePhraseLabel();
        updatePlayersLabel();

        // Check for a win.
        if (Phrases.isPhraseGuessed()) {
            handleWin(currentPlayer);
        } else {
            nextPlayer();
        }
    }
    
    //Handles the win condition and prompts the user to play again.
    private void handleWin(Players winner) {
        JOptionPane.showMessageDialog(this, "Congratulations " + winner.getFirstName() + "! You solved the phrase!", "Round Over!", JOptionPane.INFORMATION_MESSAGE);
        int choice = JOptionPane.showConfirmDialog(this, "Would you like to play another round?", "Play Again?", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            resetForNewRound();
        } else {
            // End the game by disabling controls.
            guessButton.setEnabled(false);
            setPhraseButton.setEnabled(false);
            addPlayerButton.setEnabled(false);
            turnLabel.setText("Game Over! Thanks for playing!");
        }
    }

    //Updates the label that displays the list of players and their money.
    private void updatePlayersLabel() {
        StringBuilder sb = new StringBuilder("<html>Players:<br>"); // Using HTML for multiline label.
        if (playerList.isEmpty()) {
            sb.append("No players have been added yet.");
        } else {
            for (Players p : playerList) {
                sb.append(p.toString()).append("<br>");
            }
        }
        sb.append("</html>");
        playersLabel.setText(sb.toString());
    }

    //Updates the label showing the current state of the phrase.
    private void updatePhraseLabel() {
        phraseLabel.setText("Phrase: " + Phrases.getPlayingPhrase());
    }

    //Advances to the next player and updates the turn indicator.
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        if (currentPlayerIndex == 0) {
            isMoneyRound = !isMoneyRound;
            String prizeType = isMoneyRound ? "Cash" : "Physical Prizes";
            JOptionPane.showMessageDialog(this, "Prize type has switched to: " + prizeType, "Prize Switch", JOptionPane.INFORMATION_MESSAGE);
        }
        turnLabel.setText("Turn: " + playerList.get(currentPlayerIndex).getFirstName());
    }
    //Checks if the game has enough information (players and a phrase) to start.   
    private void checkGameState() {
        if (!playerList.isEmpty() && Phrases.getPlayingPhrase() != null && !Phrases.getPlayingPhrase().isEmpty()) {
            guessButton.setEnabled(true);
            setPhraseButton.setEnabled(false); // Phrase is set, disable until round is over.
            turnLabel.setText("Turn: " + playerList.get(currentPlayerIndex).getFirstName());
        }
    }
    //Resets the game state for a new round.
    private void resetForNewRound() {
        // Reset player money.
        for (Players p : playerList) {
            p.setMoney(1000);
        }
        currentPlayerIndex = 0;
        isMoneyRound = true;
        
        // Update labels and button states.
        phraseLabel.setText("Phrase: [Click 'Set Host & Phrase' for new round]");
        updatePlayersLabel();
        turnLabel.setText("Setup new round!");
        guessButton.setEnabled(false);
        setPhraseButton.setEnabled(true);
    }

    public static void main(String[] args) {
        // Ensures the GUI is created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(GUI::new);
    }
}