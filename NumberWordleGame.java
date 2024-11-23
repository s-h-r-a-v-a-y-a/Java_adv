import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberWordleGame extends JFrame {
    private static final int NUMBER_LENGTH = 5; // Length of the target number
    private static final int MAX_ATTEMPTS = 6; // Maximum attempts allowed

    private String targetNumber;
    private int attempts;

    private final JTextField guessInput = new JTextField(10);
    private final JPanel feedbackPanel = new JPanel();
    private final JPanel liveInputPanel = new JPanel();
    private final JButton checkButton = new JButton("Check");
    private final JButton restartButton = new JButton("Restart");
    private final JLabel attemptsLabel = new JLabel("Attempts: 0/" + MAX_ATTEMPTS);
    private final JLabel encouragementLabel = new JLabel("Focus and guess the number!");

    public NumberWordleGame() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        targetNumber = generateRandomNumber(NUMBER_LENGTH);
        attempts = 0;
        feedbackPanel.removeAll();
        liveInputPanel.removeAll();
        feedbackPanel.revalidate();
        feedbackPanel.repaint();
        liveInputPanel.revalidate();
        liveInputPanel.repaint();
        checkButton.setEnabled(true);
        restartButton.setEnabled(false);
        attemptsLabel.setText("Attempts: 0/" + MAX_ATTEMPTS);
        encouragementLabel.setText("Focus and guess the number!");
        guessInput.setText("");
        guessInput.requestFocusInWindow();
        System.out.println("Target Number (for testing): " + targetNumber); // Debugging
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

        // Live Input Panel
        liveInputPanel.setLayout(new FlowLayout());
        liveInputPanel.setBorder(BorderFactory.createTitledBorder("Live Input"));
        add(liveInputPanel, BorderLayout.WEST);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        guessInput.setFont(new Font("Arial", Font.PLAIN, 16));
        guessInput.setPreferredSize(new Dimension(150, 30));
        checkButton.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(new JLabel("Your Guess:"));
        inputPanel.add(guessInput);
        inputPanel.add(checkButton);

        // Bottom Panel
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
        checkButton.addActionListener(e -> checkGuess());
        restartButton.addActionListener(e -> initializeGame());
        guessInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateLiveInput(guessInput.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkGuess();  // Check guess when 'Enter' is pressed
                }
            }
        });
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
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
        guessInput.requestFocusInWindow();
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

    private void updateLiveInput(String currentInput) {
        liveInputPanel.removeAll();
        for (int i = 0; i < currentInput.length(); i++) {
            char digit = currentInput.charAt(i);
            JLabel liveLabel = new JLabel(String.valueOf(digit));
            liveLabel.setFont(new Font("Arial", Font.BOLD, 18));
            liveLabel.setPreferredSize(new Dimension(40, 40));
            liveLabel.setOpaque(true);
            liveLabel.setBackground(Color.LIGHT_GRAY);
            liveLabel.setHorizontalAlignment(SwingConstants.CENTER);
            liveInputPanel.add(liveLabel);
        }
        liveInputPanel.revalidate();
        liveInputPanel.repaint();
    }

    private void endGame() {
        checkButton.setEnabled(false);
        restartButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberWordleGame game = new NumberWordleGame();
            game.setVisible(true);
        });
    }
}
