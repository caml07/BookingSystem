package com.cabbooking.ui;

import com.cabbooking.models.User;
import com.cabbooking.services.AuthService;
import com.cabbooking.utils.exceptions.AuthException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterPanel extends JPanel {
    private final MainFrame mainFrame;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        initUI();
    }

    private void initUI() {
        // Title Label
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Name Label and Field
        JLabel nameLabel = new JLabel("Nombre:");
        nameField = new JTextField(20);

        // Email Label and Field
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(20);

        // Role Label and Combo
        JLabel roleLabel = new JLabel("Rol:");
        roleCombo = new JComboBox<>(new String[]{"USUARIO", "CONDUCTOR"});

        // Register Button
        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(this::handleRegister);

        // Back Button
        JButton backButton = new JButton("Volver al Login");
        backButton.addActionListener(e -> mainFrame.switchToLoginPanel());

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
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(roleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(roleCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        gbc.gridy = 6;
        add(backButton, gbc);
    }

    private void handleRegister(ActionEvent e) {
        try {
            User newUser = new User(
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    com.cabbooking.enums.RolUsuario.valueOf(roleCombo.getSelectedItem().toString())
            );
            AuthService.register(newUser);
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.switchToLoginPanel();
        } catch (AuthException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
