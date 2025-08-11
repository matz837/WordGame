import javax.swing.*;
import java.awt.*;
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
    private final JButton guessButton;
    private final JTextArea messageArea;
    private final JCheckBox saveMessagesCheckbox;

    public GUI() {
        // --- Initialize Game Data ---
        this.playerList = new ArrayList<>();
        this.host = new Hosts("Game", "Master");
        this.currentPlayerIndex = 0;
        this.isMoneyRound = true;

        // --- Frame Setup ---
        setTitle("Wheel of Fortune");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Use BorderLayout for main structure
        setLayout(new BorderLayout(10, 10));

        // --- Menu Bar ---
        createMenuBar();

        // --- Panel for Game Info (North) ---
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        this.turnLabel = new JLabel("Setup the game to begin!", SwingConstants.CENTER);
        this.hostLabel = new JLabel("Host: " + host.getFirstName(), SwingConstants.CENTER);
        this.phraseLabel = new JLabel("Phrase: [Start a New Round to Begin]", SwingConstants.CENTER);
        
        infoPanel.add(turnLabel);
        infoPanel.add(hostLabel);
        infoPanel.add(phraseLabel);

        // --- Panel for Player List (West) ---
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        this.playersLabel = new JLabel("<html>No players yet.</html>");
        playerPanel.add(playersLabel, BorderLayout.NORTH);

        // --- Message Area for Game Log (Center) ---
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
        this.messageArea = new JTextArea("Welcome to the Game!\n");
        this.messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);

        // --- Controls Panel (South) ---
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.guessButton = new JButton("Guess a Letter");
        guessButton.addActionListener(e -> takeTurn());
        guessButton.setEnabled(false);

        this.saveMessagesCheckbox = new JCheckBox("Save Messages");
        saveMessagesCheckbox.setToolTipText("If checked, new messages will be added to the log. If unchecked, they will replace the old message.");
        
        controlsPanel.add(guessButton);
        controlsPanel.add(saveMessagesCheckbox);

        // --- Add Panels to Frame ---
        add(infoPanel, BorderLayout.NORTH);
        add(playerPanel, BorderLayout.WEST);
        add(messagePanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);

        // Make the frame visible
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    //Creates and configures the JMenuBar and its items.
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // --- Game Menu ---
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        JMenuItem addPlayerMenuItem = new JMenuItem("Add Player");
        addPlayerMenuItem.addActionListener(e -> addPlayer());

        JMenuItem startRoundMenuItem = new JMenuItem("Start New Round");
        startRoundMenuItem.addActionListener(e -> startNewRound());

        gameMenu.add(addPlayerMenuItem);
        gameMenu.add(startRoundMenuItem);

        // --- About Menu ---
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');

        JMenuItem aboutLayoutMenuItem = new JMenuItem("About Layout");
        aboutLayoutMenuItem.addActionListener(e -> showLayoutInfo());

        aboutMenu.add(aboutLayoutMenuItem);

        menuBar.add(gameMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
    }

    //Logs a message to the text area, respecting the saveMessagesCheckbox.
    private void logMessage(String message) {
        if (saveMessagesCheckbox.isSelected()) {
            messageArea.append(message + "\n");
        } else {
            messageArea.setText(message + "\n");
        }
        // Auto-scroll to the bottom
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }

    //Prompts for and adds a new player to the game.
    private void addPlayer() {
        String name = JOptionPane.showInputDialog(this, "Enter player's name:", "Add Player", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            playerList.add(new Players(name.trim()));
            updatePlayersLabel();
            checkGameState();
            logMessage("Player " + name.trim() + " has joined the game.");
        }
    }

    //Sets up a new round by assigning a host and a secret phrase.
    private void startNewRound() {
        String hostName = JOptionPane.showInputDialog(this, "Enter host's name:", "Set Host", JOptionPane.PLAIN_MESSAGE);
        if (hostName != null && !hostName.trim().isEmpty()) {
            this.host = new Hosts(hostName.trim(), "");
            hostLabel.setText("Host: " + host.getFirstName());
        }

        // Host selects a phrase automatically now
        host.setNewPhrase();
        logMessage(host.getFirstName() + " has selected a new secret phrase.");
        
        // Reset player money and state for the new round
        for (Players p : playerList) {
            p.setMoney(1000);
        }
        currentPlayerIndex = 0;
        isMoneyRound = true;

        updatePhraseLabel();
        updatePlayersLabel();
        checkGameState();
    }
    
    //Executes a single player's turn.
    private void takeTurn() {
        Players currentPlayer = playerList.get(currentPlayerIndex);

        if (currentPlayer.getMoney() <= 0 && isMoneyRound) {
            logMessage(currentPlayer.getFirstName() + " is out of money and must skip a turn.");
            nextPlayer();
            return;
        }

        String guess = JOptionPane.showInputDialog(this, currentPlayer.getFirstName() + ", please guess a letter:", "Guess a Letter", JOptionPane.PLAIN_MESSAGE);
        if (guess == null) return; // Player cancelled

        boolean letterFound;
        Award award = isMoneyRound ? new Money() : new Physical();

        try {
            letterFound = Phrases.findLetters(guess.trim());
        } catch (MultipleLettersException ex) {
            letterFound = false;
            logMessage("Error: " + ex.getMessage() + ". This counts as an incorrect guess.");
        }

        // Handle winnings and update game state
        if (letterFound) {
            logMessage("Good guess! '" + guess.toUpperCase() + "' is in the phrase.");
        } else {
            logMessage("Sorry, '" + guess.toUpperCase() + "' is not in the phrase.");
        }

        // Apply award logic
        if (isMoneyRound) {
            int moneyChange = ((Money) award).displayWinnings(currentPlayer, letterFound);
            currentPlayer.setMoney(currentPlayer.getMoney() + moneyChange);
            logMessage(currentPlayer.getFirstName() + (moneyChange >= 0 ? " won $" : " lost $") + Math.abs(moneyChange));
        } else {
            // We need to cast to get the prize details for the log
            String prize = ((Physical) award).getPrizeForDisplay(letterFound);
            logMessage(currentPlayer.getFirstName() + " " + prize);
        }
        
        // Ensure money never drops below zero
        if (currentPlayer.getMoney() < 0) {
            currentPlayer.setMoney(0);
        }
        
        updatePhraseLabel();
        updatePlayersLabel();

        // Check for a win condition
        if (Phrases.isPhraseGuessed()) {
            handleWin(currentPlayer);
        } else {
            nextPlayer();
        }
    }
    
    //Handles the win condition and prompts to play again.
    private void handleWin(Players winner) {
        logMessage("Congratulations " + winner.getFirstName() + "! You solved the phrase!");
        logMessage("The phrase was: " + Phrases.getPlayingPhrase());
        JOptionPane.showMessageDialog(this, "Congratulations " + winner.getFirstName() + "! You solved the phrase!", "Round Over!", JOptionPane.INFORMATION_MESSAGE);
        
        int choice = JOptionPane.showConfirmDialog(this, "Would you like to play another round?", "Play Again?", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            resetForNewGame();
        } else {
            // End game
            guessButton.setEnabled(false);
            turnLabel.setText("Game Over! Thanks for playing!");
        }
    }

    //Updates the label that displays the players and their scores.
    private void updatePlayersLabel() {
        StringBuilder sb = new StringBuilder("<html>");
        if (playerList.isEmpty()) {
            sb.append("No players yet.");
        } else {
            for (Players p : playerList) {
                sb.append(p.toString()).append("<br>");
            }
        }
        sb.append("</html>");
        playersLabel.setText(sb.toString());
    }

    //Updates the label showing the current masked phrase.
    private void updatePhraseLabel() {
        phraseLabel.setText("Phrase: " + Phrases.getPlayingPhrase());
    }

    //Advances to the next player and updates the turn indicator.
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        if (currentPlayerIndex == 0) {
            isMoneyRound = !isMoneyRound;
            String prizeType = isMoneyRound ? "Cash" : "Physical Prizes";
            logMessage("--- Round Complete! Prize type has switched to: " + prizeType + " ---");
        }
        turnLabel.setText("Turn: " + playerList.get(currentPlayerIndex).getFirstName());
    }
    
    //Checks if the game has enough players and a phrase to start.
    private void checkGameState() {
        if (!playerList.isEmpty() && Phrases.getPlayingPhrase() != null && !Phrases.getPlayingPhrase().isEmpty()) {
            guessButton.setEnabled(true);
            turnLabel.setText("Turn: " + playerList.get(currentPlayerIndex).getFirstName());
            logMessage("The game is ready. " + playerList.get(currentPlayerIndex).getFirstName() + " starts!");
        }
    }
    
    //Resets the game state for a completely new game, clearing the log.
    private void resetForNewGame() {
        // Clear message log for new game
        messageArea.setText("");
        
        // Prompt to start a new round which handles all setup
        logMessage("Starting a new game! Use the 'Game' menu to begin.");
        phraseLabel.setText("Phrase: [Start a New Round to Begin]");
        turnLabel.setText("Setup new game!");
        guessButton.setEnabled(false);
    }

    
    //Displays a dialog with information about the layout choice.
    private void showLayoutInfo() {
        String info = "NORTH: Game status information (turn, host, phrase).\n"
                    + "WEST: A persistent list of players and their scores.\n"
                    + "CENTER: The main game log, which needs flexible space.\n"
                    + "SOUTH: Primary user controls.\n\n";
        JOptionPane.showMessageDialog(this, info, "About Layout", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Ensures the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(GUI::new);
    }
}