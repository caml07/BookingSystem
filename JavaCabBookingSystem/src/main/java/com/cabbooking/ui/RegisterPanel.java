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
    private JTextField vehicleField;
    private JTextField phoneField;
    private JPanel driverFieldsPanel;

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
        roleCombo.addActionListener(e -> toggleDriverFields());
        
        // Panel para campos específicos de conductores
        driverFieldsPanel = new JPanel(new GridBagLayout());
        driverFieldsPanel.setBorder(BorderFactory.createTitledBorder("Información de Conductor"));
        
        JLabel vehicleLabel = new JLabel("Vehículo:");
        vehicleField = new JTextField(20);
        
        JLabel phoneLabel = new JLabel("Teléfono:");
        phoneField = new JTextField(20);
        
        GridBagConstraints driverGbc = new GridBagConstraints();
        driverGbc.insets = new Insets(5, 5, 5, 5);
        driverGbc.anchor = GridBagConstraints.WEST;
        driverGbc.gridx = 0;
        driverGbc.gridy = 0;
        driverFieldsPanel.add(vehicleLabel, driverGbc);
        
        driverGbc.gridx = 1;
        driverGbc.fill = GridBagConstraints.HORIZONTAL;
        driverFieldsPanel.add(vehicleField, driverGbc);
        
        driverGbc.gridx = 0;
        driverGbc.gridy = 1;
        driverGbc.fill = GridBagConstraints.NONE;
        driverFieldsPanel.add(phoneLabel, driverGbc);
        
        driverGbc.gridx = 1;
        driverGbc.fill = GridBagConstraints.HORIZONTAL;
        driverFieldsPanel.add(phoneField, driverGbc);
        
        driverFieldsPanel.setVisible(false);

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
        add(driverFieldsPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        gbc.gridy = 7;
        add(backButton, gbc);
    }

    private void toggleDriverFields() {
        String selectedRole = (String) roleCombo.getSelectedItem();
        driverFieldsPanel.setVisible("CONDUCTOR".equals(selectedRole));
        revalidate();
        repaint();
    }
    
    private void handleRegister(ActionEvent e) {
        try {
            // Get the selected role as a string
            String selectedRole = (String) roleCombo.getSelectedItem();
            
            // Validar campos según el rol
            if ("CONDUCTOR".equals(selectedRole)) {
                if (vehicleField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar información del vehículo", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phoneField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un número de teléfono", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            User newUser = new User(
                    nameField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    selectedRole
            );

            if ("CONDUCTOR".equals(selectedRole)) {
                AuthService.registerDriver(newUser, vehicleField.getText(), phoneField.getText());
            } else {
                AuthService.register(newUser);
            }
            
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.switchToLoginPanel();
        } catch (AuthException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
