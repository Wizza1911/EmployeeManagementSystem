package GUI;

import java.awt.*;
import javax.swing.*;
import Backend.LoginManager;
import FileHandler.AccountHandler;


public class Login {
    private final JTextField usernameField=new JTextField(25);
    private final JPasswordField passwordField= new JPasswordField(25);
    private final JButton loginButton=new JButton("Login");
    private final JButton exitButton=new JButton("Exit");
    private final JFrame loginFrame=new JFrame();
    private final LoginManager loginManager=new LoginManager(new AccountHandler());
    public Login(){
        initializeLoginGui();
    }
    public void initializeLoginGui() {
        // Frame setup
        loginFrame.setTitle("Employee Management System");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setMinimumSize(new Dimension(460, 340));
        loginFrame.setResizable(false);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(240, 245, 250));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel cardPanel = new JPanel(new BorderLayout(0, 20));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 240)),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(15, 20, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Please sign in to continue");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 120, 140));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitleLabel);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(new Color(15, 20, 40));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(new Color(15, 20, 40));

        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(200, 32));
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(200, 32));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        Font buttonFont = new Font("SansSerif", Font.BOLD, 13);

        loginButton.setText("Login");
        loginButton.setFont(buttonFont);
        loginButton.setPreferredSize(new Dimension(130, 38));
        loginButton.setBackground(new Color(40, 167, 69));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        exitButton.setText("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setPreferredSize(new Dimension(130, 38));
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        loginButton.addActionListener(e -> checkLogin());
        passwordField.addActionListener(e -> checkLogin());
        exitButton.addActionListener(e -> loginFrame.dispose());

        cardPanel.add(titlePanel, BorderLayout.NORTH);
        cardPanel.add(inputPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        rootPanel.add(cardPanel, BorderLayout.CENTER);
        loginFrame.setContentPane(rootPanel);
        loginFrame.getRootPane().setDefaultButton(loginButton);

        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
    
    private boolean isValidUser(String username, String password) {
        return loginManager.checkAccount(username, password);
    }
     private void checkLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame,"Please enter both username and password.","Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isValidUser(username, password)) {
            loginFrame.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(loginFrame,
                    "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
}