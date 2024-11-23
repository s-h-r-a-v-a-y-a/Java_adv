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

        // Panels for target and user colors
        JPanel colorPanel = createColorPanel();
        JPanel controlPanel = createControlPanel();

        // Random color for the target
        Random random = new Random();
        Color targetColor = generateRandomColor(random);

        // Adding target and user color boxes
        colorPanel.add(createColorBoxPanel("Match This Color", targetColor));
        JPanel userColorPanel = createColorBoxPanel("Your Color", Color.BLACK);
        colorPanel.add(userColorPanel);

        // Add components to the main frame
        frame.add(colorPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Update sliders and labels dynamically
        updateControlPanelListeners(controlPanel, userColorPanel, targetColor);

        // Show the frame
        frame.setVisible(true);
    }

    private static JPanel createColorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }

    private static JPanel createColorBoxPanel(String title, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 250));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2), title,
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));
        return panel;
    }

    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        controlPanel.setBackground(Color.DARK_GRAY);

        // Sliders
        JSlider redSlider = createColorSlider();
        JSlider greenSlider = createColorSlider();
        JSlider blueSlider = createColorSlider();

        // Labels
        JLabel redLabel = new JLabel("Red:", JLabel.RIGHT);
        JLabel greenLabel = new JLabel("Green:", JLabel.RIGHT);
        JLabel blueLabel = new JLabel("Blue:", JLabel.RIGHT);

        styleLabel(redLabel);
        styleLabel(greenLabel);
        styleLabel(blueLabel);

        JLabel hexLabel = new JLabel("Your Color Hex: #000000", SwingConstants.CENTER);
        styleHexLabel(hexLabel);

        // Buttons
        JButton checkButton = new JButton("Check");
        JButton restartButton = new JButton("Restart");
        styleButton(checkButton);
        styleButton(restartButton);

        // Adding components
        controlPanel.add(redLabel);
        controlPanel.add(redSlider);
        controlPanel.add(greenLabel);
        controlPanel.add(greenSlider);
        controlPanel.add(blueLabel);
        controlPanel.add(blueSlider);
        controlPanel.add(checkButton);
        controlPanel.add(restartButton);

        // Add sliders and labels to control panel
        controlPanel.putClientProperty("redSlider", redSlider);
        controlPanel.putClientProperty("greenSlider", greenSlider);
        controlPanel.putClientProperty("blueSlider", blueSlider);
        controlPanel.putClientProperty("hexLabel", hexLabel);
        controlPanel.putClientProperty("checkButton", checkButton);
        controlPanel.putClientProperty("restartButton", restartButton);

        return controlPanel;
    }

    private static JSlider createColorSlider() {
        JSlider slider = new JSlider(0, 255, 0);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setForeground(Color.WHITE);
        return slider;
    }

    private static void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private static void styleHexLabel(JLabel label) {
        label.setFont(new Font("Courier New", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
    }

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

    private static Color generateRandomColor(Random random) {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private static void updateControlPanelListeners(JPanel controlPanel, JPanel userColorPanel, Color targetColor) {
        JSlider redSlider = (JSlider) controlPanel.getClientProperty("redSlider");
        JSlider greenSlider = (JSlider) controlPanel.getClientProperty("greenSlider");
        JSlider blueSlider = (JSlider) controlPanel.getClientProperty("blueSlider");
        JLabel hexLabel = (JLabel) controlPanel.getClientProperty("hexLabel");
        JButton checkButton = (JButton) controlPanel.getClientProperty("checkButton");
        JButton restartButton = (JButton) controlPanel.getClientProperty("restartButton");

        // Update user color and hex label dynamically
        redSlider.addChangeListener(e -> updateColor(redSlider, greenSlider, blueSlider, userColorPanel, hexLabel));
        greenSlider.addChangeListener(e -> updateColor(redSlider, greenSlider, blueSlider, userColorPanel, hexLabel));
        blueSlider.addChangeListener(e -> updateColor(redSlider, greenSlider, blueSlider, userColorPanel, hexLabel));

        // Check color matching
        checkButton.addActionListener(e -> {
            Color userColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
            if (userColor.equals(targetColor)) {
                JOptionPane.showMessageDialog(null, "Congratulations! You matched the color!");
            } else {
                JOptionPane.showMessageDialog(null, "Try again! Keep adjusting the sliders.");
            }
        });

        // Restart game
        restartButton.addActionListener(e -> {
            redSlider.setValue(0);
            greenSlider.setValue(0);
            blueSlider.setValue(0);
            hexLabel.setText("Your Color Hex: #000000");
            userColorPanel.setBackground(Color.BLACK);
        });
    }

    private static void updateColor(JSlider redSlider, JSlider greenSlider, JSlider blueSlider, JPanel userColorPanel, JLabel hexLabel) {
        int red = redSlider.getValue();
        int green = greenSlider.getValue();
        int blue = blueSlider.getValue();
        Color userColor = new Color(red, green, blue);
        userColorPanel.setBackground(userColor);
        hexLabel.setText(String.format("Your Color Hex: #%02X%02X%02X", red, green, blue));
    }
}
