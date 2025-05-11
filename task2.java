

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class task2 extends JFrame implements ActionListener {

    private Random random;
    private int targetNumber;
    private int attemptsLeft;
    private String difficultyLevel;

    private JLabel instructionLabel;
    private JTextField guessField;
    private JButton tryButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;

    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JPanel gamePanel;

    public task2() {
        random = new Random();
        difficultyLevel = "";

        // Frame settings
        setTitle("Guess The Number Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window

        mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel);

        setupWelcomeScreen();
        setVisible(true);
    }

    private void setupWelcomeScreen() {
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Guess The Number Game", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomePanel.add(heading, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton easyButton = createStyledButton("Easy");
        easyButton.addActionListener(e -> transitionToGame("Easy"));

        JButton mediumButton = createStyledButton("Medium");
        mediumButton.addActionListener(e -> transitionToGame("Medium"));

        JButton hardButton = createStyledButton("Hard");
        hardButton.addActionListener(e -> transitionToGame("Hard"));

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        gbc.gridy = 1;
        welcomePanel.add(buttonPanel, gbc);

        mainPanel.add(welcomePanel, "Welcome");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 40));
        button.setBackground(new Color(255, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 70, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 100, 100));
            }
        });
        return button;
    }

    private void transitionToGame(String difficulty) {
        // Smooth transition
        Timer timer = new Timer(10, null);
        final float[] opacity = {1.0f};
        timer.addActionListener(e -> {
            opacity[0] -= 0.05f;
            welcomePanel.setOpaque(true);
            welcomePanel.repaint();
            welcomePanel.setBackground(new Color(245, 245, 245, (int)(opacity[0] * 255)));
            if (opacity[0] <= 0) {
                timer.stop();
                startGame(difficulty);
            }
        });
        timer.start();
    }
    private void startGame(String difficulty) {
        difficultyLevel = difficulty;

        int upperLimit = 100;
        int maxAttempts = 10;
        if (difficulty.equals("Easy")) {
            upperLimit = 50;
            maxAttempts = 7;
        } else if (difficulty.equals("Hard")) {
            upperLimit = 200;
            maxAttempts = 12;
        }

        targetNumber = random.nextInt(upperLimit) + 1;
        attemptsLeft = maxAttempts;

        gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        instructionLabel = new JLabel("Enter a number between 1 and " + upperLimit);
        instructionLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gamePanel.add(instructionLabel, gbc);

        guessField = new JTextField(15);
        guessField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gamePanel.add(guessField, gbc);

        tryButton = new JButton("Try!");
        tryButton.setFocusPainted(false);
        tryButton.setBackground(new Color(255, 69, 0));
        tryButton.setForeground(Color.WHITE);
        tryButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        tryButton.addActionListener(this);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gamePanel.add(tryButton, gbc);

        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gamePanel.add(feedbackLabel, gbc);

        attemptsLabel = new JLabel("Attempts left: " + attemptsLeft);
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gamePanel.add(attemptsLabel, gbc);

        // Support Enter Key Press
        guessField.addActionListener(this);

        mainPanel.add(gamePanel, "Game");

        // Switch to Game Panel
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Game");

        fadeIn(gamePanel);
    }

    private void fadeIn(JPanel panel) {
        Timer timer = new Timer(10, null);
        final float[] opacity = {0f};
        timer.addActionListener(e -> {
            opacity[0] += 0.05f;
            if (opacity[0] >= 1) {
                opacity[0] = 1;
                timer.stop();
            }
            panel.setOpaque(true);
            panel.repaint();
            panel.setBackground(new Color(245, 245, 245, (int)(opacity[0] * 255)));
        });
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userInput = guessField.getText().trim();

        if (!userInput.matches("\\d+")) {
            feedbackLabel.setText("Please enter a valid number.");
            return;
        }

        int guess = Integer.parseInt(userInput);
        attemptsLeft--;

        if (guess < targetNumber) {
            feedbackLabel.setText("Too low! Try higher.");
        } else if (guess > targetNumber) {
            feedbackLabel.setText("Too high! Try lower.");
        } else {
            feedbackLabel.setText("Correct! You Win!");
            tryButton.setEnabled(false);
            guessField.setEditable(false);
        }

        if (attemptsLeft == 0 && guess != targetNumber) {
            feedbackLabel.setText("No attempts left! Number was: " + targetNumber);
            tryButton.setEnabled(false);
            guessField.setEditable(false);
        }

        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        guessField.setText("");
    }

    public static void main(String[] args) {
        new task2();
    }
}
