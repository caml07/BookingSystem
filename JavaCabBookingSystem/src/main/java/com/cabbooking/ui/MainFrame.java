package com.cabbooking.ui;

import com.cabbooking.models.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel containerPanel;
    private User currentUser;

    public MainFrame() {
        setTitle("Cab Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Panels
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel(this);

        // Add initial panels to container
        containerPanel.add(loginPanel, "Login");
        containerPanel.add(registerPanel, "Register");

        add(containerPanel);

        // Start with login panel
        cardLayout.show(containerPanel, "Login");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void switchToLoginPanel() {
        cardLayout.show(containerPanel, "Login");
    }

    public void switchToRegisterPanel() {
        cardLayout.show(containerPanel, "Register");
    }

    public void switchToMapPanel() {
        // Create the booking panel with the current user if it doesn't exist
        if (containerPanel.getComponentCount() <= 2) { // Login and Register only
            BookingPanel bookingPanel = new BookingPanel(currentUser, this);
            containerPanel.add(bookingPanel, "Map");
        }
        cardLayout.show(containerPanel, "Map");
    }

    public void mostrarHistorial(String userId) {
        // Remove any existing historial panel
        try {
            for (Component comp : containerPanel.getComponents()) {
                if (comp instanceof HistorialPanel) {
                    containerPanel.remove(comp);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a new historial panel
        HistorialPanel historialPanel = new HistorialPanel(userId, this);
        containerPanel.add(historialPanel, "Historial");
        cardLayout.show(containerPanel, "Historial");
    }
}
