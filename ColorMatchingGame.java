 import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Random;

public class ColorMatchingGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ColorMatchingGame::createAndShowGame);
    }

    private static void createAndShowGame() {
        // Create main frame
        JFrame frame = new JFrame("Color Matching Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panels for target and user colors
        JPanel targetColorPanel = createColorPanel("Match This Color");
        JPanel userColorPanel = createColorPanel("Your Color");

        // Random color for the target
        Random random = new Random();
        Color targetColor = generateRandomColor(random);
        targetColorPanel.setBackground(targetColor);

        // Control Panel for sliders and buttons
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JSlider redSlider = createSlider();
        JSlider greenSlider = createSlider();
        JSlider blueSlider = createSlider();
        JLabel hexLabel = new JLabel("Your Color Hex: #000000", SwingConstants.CENTER);

        // Buttons for "Check" and "Restart"
        JButton checkButton = new JButton("Check");
        JButton restartButton = new JButton("Restart");
        controlPanel.add(new JLabel("Red:")); controlPanel.add(redSlider);
        controlPanel.add(new JLabel("Green:")); controlPanel.add(greenSlider);
        controlPanel.add(new JLabel("Blue:")); controlPanel.add(blueSlider);
        controlPanel.add(checkButton); controlPanel.add(restartButton);

        // Add components to the main frame
        frame.add(targetColorPanel, BorderLayout.WEST);
        frame.add(userColorPanel, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(hexLabel, BorderLayout.NORTH);

        // ChangeListener to update user color in real-time
        ChangeListener updateUserColor = e -> updateColorDisplay(
            redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), userColorPanel, hexLabel
        );

        // Attach listeners
        redSlider.addChangeListener(updateUserColor);
        greenSlider.addChangeListener(updateUserColor);
        blueSlider.addChangeListener(updateUserColor);

        // ActionListener for "Check" button
        checkButton.addActionListener(e -> {
            if (colorsMatch(targetColor, userColorPanel.getBackground())) {
                JOptionPane.showMessageDialog(frame, "Congratulations! You matched the color!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Try again! Keep adjusting.",
                        "Mismatch", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ActionListener for "Restart" button
        restartButton.addActionListener(e -> {
            targetColor = generateRandomColor(random);
            targetColorPanel.setBackground(targetColor);
            resetGame(redSlider, greenSlider, blueSlider, userColorPanel, hexLabel);
        });

        // Show the frame
        frame.setVisible(true);
    }

    // Helper Method: Creates a color panel with a border
    private static JPanel createColorPanel(String title) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    // Helper Method: Creates a JSlider
    private static JSlider createSlider() {
        JSlider slider = new JSlider(0, 255, 0);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        return slider;
    }

    // Helper Method: Generates a random color
    private static Color generateRandomColor(Random random) {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // Helper Method: Updates the user's color display and hex code
    private static void updateColorDisplay(int red, int green, int blue, JPanel userColorPanel, JLabel hexLabel) {
        Color userColor = new Color(red, green, blue);
        userColorPanel.setBackground(userColor);
        hexLabel.setText(String.format("Your Color Hex: #%02X%02X%02X", red, green, blue));
    }

    // Helper Method: Resets the game to its initial state
    private static void resetGame(JSlider redSlider, JSlider greenSlider, JSlider blueSlider, 
                                  JPanel userColorPanel, JLabel hexLabel) {
        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);
        userColorPanel.setBackground(Color.BLACK);
        hexLabel.setText("Your Color Hex: #000000");
    }

    // Helper Method: Checks if two colors match
    private static boolean colorsMatch(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() &&
               c1.getGreen() == c2.getGreen() &&
               c1.getBlue() == c2.getBlue();
    }
}
