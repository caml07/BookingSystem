package com.cabbooking.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel containerPanel;

    public MainFrame() {
        setTitle("Cab Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Panels
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel(this);
        MapPanel mapPanel = new MapPanel();
        HistorialPanel historialPanel = new HistorialPanel("userId_example", this);

        // Add Panels to container
        containerPanel.add(loginPanel, "Login");
        containerPanel.add(registerPanel, "Register");
        containerPanel.add(mapPanel, "Map");
        containerPanel.add(historialPanel, "Historial");

        add(containerPanel);
    }

    public void switchToLoginPanel() {
        cardLayout.show(containerPanel, "Login");
    }

    public void switchToRegisterPanel() {
        cardLayout.show(containerPanel, "Register");
    }

    public void switchToMapPanel() {
        cardLayout.show(containerPanel, "Map");
    }

    public void switchToHistorialPanel() {
        cardLayout.show(containerPanel, "Historial");
    }
}
