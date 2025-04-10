package com.cabbooking.ui;

import com.cabbooking.services.AuthService;
import com.cabbooking.models.User;
import com.cabbooking.utils.exceptions.AuthException;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1));

        emailTextField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Entrar");

        inputPanel.add(new JLabel("Correo electrónico:"));
        inputPanel.add(emailTextField);
        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(passwordField);
        inputPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());

            try {
                User user = AuthService.login(email, password);
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                mainFrame.switchToMapPanel();
                JOptionPane.showMessageDialog(this, "¡Bienvenido " + user.getName() + "!");
            } catch (AuthException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
