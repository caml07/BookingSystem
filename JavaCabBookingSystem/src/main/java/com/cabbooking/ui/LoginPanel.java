package com.cabbooking.ui;

import com.cabbooking.services.AuthService;
import com.cabbooking.models.User;
import com.cabbooking.utils.exceptions.AuthException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        initUI();
    }

    private void initUI() {
        // Title Label
        JLabel titleLabel = new JLabel("Cab Booking System");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Email Label and Field
        JLabel emailLabel = new JLabel("Correo:");
        emailTextField = new JTextField(20);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(20);

        // Login Button
        loginButton = new JButton("Entrar");
        loginButton.addActionListener(this::handleLogin);

        // Register Button
        registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.switchToRegisterPanel();
        });

        // Layout using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());

        try {
            User user = AuthService.login(email, password);
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setCurrentUser(user);
            mainFrame.switchToMapPanel();
            JOptionPane.showMessageDialog(this, "¡Bienvenido " + user.getName() + "!");
        } catch (AuthException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
