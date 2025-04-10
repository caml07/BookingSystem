package com.cabbooking.ui;

import com.cabbooking.models.Ride;
import com.cabbooking.services.BookingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistorialPanel extends JPanel {
    private final MainFrame mainFrame;
    private final String userId;

    public HistorialPanel(String userId, MainFrame mainFrame) {
        this.userId = userId;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initUI();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("Historial de Viajes");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        List<Ride> rides = BookingService.getRidesByUser(userId);

        JPanel ridesPanel = new JPanel(new GridLayout(rides.size(), 1, 5, 5));
        rides.forEach(ride -> {
            JPanel ridePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ridePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            ridePanel.add(new JLabel("ID: " + ride.getId()));
            ridePanel.add(new JLabel("Origen: " + ride.getOrigen()));
            ridePanel.add(new JLabel("Destino: " + ride.getDestino()));
            ridePanel.add(new JLabel("Estado: " + ride.getEstado()));
            ridesPanel.add(ridePanel);
        });

        JButton regresarBtn = new JButton("Regresar");
        regresarBtn.addActionListener(e -> mainFrame.switchToMapPanel());

        add(ridesPanel, BorderLayout.CENTER);
        add(regresarBtn, BorderLayout.SOUTH);
    }
}
