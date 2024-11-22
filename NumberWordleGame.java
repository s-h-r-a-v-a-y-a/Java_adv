import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberWordleGame extends JFrame {
    private static final int NUMBER_LENGTH = 5; // Number size
    private static final int MAX_ATTEMPTS = 6; // Maximum attempts

    private String targetNumber;
    private int attempts;

    private final JTextField guessInput = new JTextField(10);
    private final JPanel feedbackPanel = new JPanel();
    private final JButton guessButton = new JButton("Guess");
    private final JButton restartButton = new JButton("Restart");
    private final JLabel attemptsLabel = new JLabel("Attempts: 0/" + MAX_ATTEMPTS);
    private final JLabel encouragementLabel = new JLabel("You can do this! Focus and guess!");

    public NumberWordleGame() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        targetNumber = generateRandomNumber(NUMBER_LENGTH);
        attempts = 0;
        feedbackPanel.removeAll();
        feedbackPanel.revalidate();
        feedbackPanel.repaint();
        guessButton.setEnabled(true);
        restartButton.setEnabled(false);
        attemptsLabel.setText("Attempts: 0/" + MAX_ATTEMPTS);
        encouragementLabel.setText("You can do this! Focus and guess!");
        System.out.println("Target Number (for testing): " + targetNumber); // Debug info
    }

    private void setupUI() {
        setTitle("Number Wordle Game");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Number Wordle Game", JLabel.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setForeground(new Color(34, 139, 34)); // Green
        headerPanel.add(title, BorderLayout.NORTH);

        // Instructions
        JLabel instructions = new JLabel(
                "<html>Guess the 5-digit number!<br>Green: Correct and in position, Yellow: Correct but wrong position, Gray: Not in the number.</html>",
                JLabel.CENTER
        );
        instructions.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(instructions, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Feedback Panel
        feedbackPanel.setLayout(new GridLayout(MAX_ATTEMPTS, 1, 5, 5));
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Your Attempts"));
        add(feedbackPanel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        guessInput.setFont(new Font("Arial", Font.PLAIN, 16));
        guessInput.setPreferredSize(new Dimension(150, 30));
        guessButton.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(new JLabel("Your Guess:"));
        inputPanel.add(guessInput);
        inputPanel.add(guessButton);

        // Encouragement Label
        encouragementLabel.setHorizontalAlignment(JLabel.CENTER);
        encouragementLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        encouragementLabel.setForeground(new Color(30, 144, 255)); // Dodger Blue

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(attemptsLabel, BorderLayout.NORTH);
        bottomPanel.add(encouragementLabel, BorderLayout.CENTER);
        bottomPanel.add(inputPanel, BorderLayout.SOUTH);

        // Restart Button
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setEnabled(false);
        JPanel restartPanel = new JPanel();
        restartPanel.add(restartButton);
        bottomPanel.add(restartPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        guessButton.addActionListener(e -> checkGuess());
        restartButton.addActionListener(e -> initializeGame());
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Generate digits 0-9
        }
        return sb.toString();
    }

    private void checkGuess() {
        String guess = guessInput.getText().trim();
        if (!isValidGuess(guess)) {
            JOptionPane.showMessageDialog(this, "Invalid input! Enter a 5-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        attempts++;
        attemptsLabel.setText("Attempts: " + attempts + "/" + MAX_ATTEMPTS);
        JPanel feedbackRow = new JPanel(new FlowLayout());
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            char guessedDigit = guess.charAt(i);
            JLabel digitLabel = createFeedbackLabel(guessedDigit, i);
            feedbackRow.add(digitLabel);
        }

        feedbackPanel.add(feedbackRow);
        feedbackPanel.revalidate();

        if (guess.equals(targetNumber)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the number in " + attempts + " attempts!", "Winner", JOptionPane.INFORMATION_MESSAGE);
            encouragementLabel.setText("Amazing job! Click 'Restart' to play again!");
            endGame();
        } else if (attempts >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(this, "Game Over! The number was: " + targetNumber, "Game Over", JOptionPane.ERROR_MESSAGE);
            encouragementLabel.setText("Don't give up! Click 'Restart' to try again!");
            endGame();
        } else {
            encouragementLabel.setText("Keep going! You're getting closer!");
        }

        guessInput.setText("");
    }

    private boolean isValidGuess(String guess) {
        return guess.length() == NUMBER_LENGTH && guess.matches("\\d+");
    }

    private JLabel createFeedbackLabel(char guessedDigit, int position) {
        JLabel digitLabel = new JLabel(String.valueOf(guessedDigit));
        digitLabel.setOpaque(true);
        digitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        digitLabel.setFont(new Font("Arial", Font.BOLD, 18));
        digitLabel.setPreferredSize(new Dimension(40, 40));

        if (guessedDigit == targetNumber.charAt(position)) {
            digitLabel.setBackground(Color.GREEN);
            digitLabel.setForeground(Color.WHITE);
        } else if (targetNumber.contains(String.valueOf(guessedDigit))) {
            digitLabel.setBackground(Color.YELLOW);
            digitLabel.setForeground(Color.BLACK);
        } else {
            digitLabel.setBackground(Color.GRAY);
            digitLabel.setForeground(Color.WHITE);
        }

        return digitLabel;
    }

    private void endGame() {
        guessButton.setEnabled(false);
        restartButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberWordleGame game = new NumberWordleGame();
            game.setVisible(true);
        });
    }
}
