import javax.swing.*;
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
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Panels for target and user colors (centered)
        JPanel colorPanel = createColorPanel();
        JPanel controlPanel = createControlPanel();

        // Random color for the target
        Random random = new Random();
        Color targetColor = generateRandomColor(random);
        colorPanel.add(createColorBoxPanel("Match This Color", targetColor), BorderLayout.NORTH);
        colorPanel.add(createColorBoxPanel("Your Color", Color.BLACK), BorderLayout.SOUTH);

        // Add components to the main frame
        frame.add(colorPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }

    // Helper Method: Creates a centered color panel for target and user colors
    private static JPanel createColorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }

    // Helper Method: Creates a color box with a label and color background
    private static JPanel createColorBoxPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 250));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    // Helper Method: Creates a control panel with sliders and buttons
    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Create sliders and buttons
        JSlider redSlider = createColorSlider("Red");
        JSlider greenSlider = createColorSlider("Green");
        JSlider blueSlider = createColorSlider("Blue");

        JLabel hexLabel = new JLabel("Your Color Hex: #000000", SwingConstants.CENTER);
        hexLabel.setFont(new Font("Courier New", Font.BOLD, 16));
        hexLabel.setForeground(Color.WHITE);

        JButton checkButton = new JButton("Check");
        JButton restartButton = new JButton("Restart");

        // Style buttons
        styleButton(checkButton);
        styleButton(restartButton);

        // Add sliders and buttons to the control panel
        controlPanel.add(new JLabel("Red:", JLabel.RIGHT));
        controlPanel.add(redSlider);
        controlPanel.add(new JLabel("Green:", JLabel.RIGHT));
        controlPanel.add(greenSlider);
        controlPanel.add(new JLabel("Blue:", JLabel.RIGHT));
        controlPanel.add(blueSlider);
        controlPanel.add(checkButton);
        controlPanel.add(restartButton);

        // Add listeners to the sliders and buttons
        addSliderListeners(redSlider, greenSlider, blueSlider, hexLabel);

        // Action listener for "Check" button
        checkButton.addActionListener(e -> handleCheckButtonAction(redSlider, greenSlider, blueSlider, hexLabel));

        // Action listener for "Restart" button
        restartButton.addActionListener(e -> handleRestartButtonAction(redSlider, greenSlider, blueSlider, hexLabel));

        return controlPanel;
    }

    // Helper Method: Creates a JSlider with color specific to its label
    private static JSlider createColorSlider(String color) {
        JSlider slider = new JSlider(0, 255, 0);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setForeground(getColorForName(color));
        return slider;
    }

    // Helper Method: Styles buttons
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(30, 144, 255)); // Dodger Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    // Helper Method: Updates the user's color display and hex code
    private static void updateColorDisplay(int red, int green, int blue, JLabel hexLabel, JPanel userColorPanel) {
        Color userColor = new Color(red, green, blue);
        userColorPanel.setBackground(userColor);
        hexLabel.setText(String.format("Your Color Hex: #%02X%02X%02X", red, green, blue));
    }

    // Helper Method: Adds listeners to sliders
    private static void addSliderListeners(JSlider redSlider, JSlider greenSlider, JSlider blueSlider, JLabel hexLabel) {
        redSlider.addChangeListener(e -> updateColorDisplay(
            redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), hexLabel
        ));
        greenSlider.addChangeListener(e -> updateColorDisplay(
            redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), hexLabel
        ));
        blueSlider.addChangeListener(e -> updateColorDisplay(
            redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), hexLabel
        ));
    }

    // Helper Method: Handles the action for the "Check" button
    private static void handleCheckButtonAction(JSlider redSlider, JSlider greenSlider, JSlider blueSlider, JLabel hexLabel) {
        Color userColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
        if (userColor.equals(generateRandomColor(new Random()))) {
            JOptionPane.showMessageDialog(null, "Congratulations! You matched the color!");
        } else {
            JOptionPane.showMessageDialog(null, "Try again! Keep adjusting the sliders.");
        }
    }

    // Helper Method: Handles the action for the "Restart" button
    private static void handleRestartButtonAction(JSlider redSlider, JSlider greenSlider, JSlider blueSlider, JLabel hexLabel) {
        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);
        hexLabel.setText("Your Color Hex: #000000");
    }

    // Helper Method: Generates a random color
    private static Color generateRandomColor(Random random) {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // Helper Method: Maps color name to Color object
    private static Color getColorForName(String color) {
        switch (color.toLowerCase()) {
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            default: return Color.BLACK;
        }
    }
}
