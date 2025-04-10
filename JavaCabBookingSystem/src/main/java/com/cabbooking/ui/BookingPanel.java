package com.cabbooking.ui;

import com.cabbooking.models.Driver;
import com.cabbooking.models.Ride;
import com.cabbooking.models.User;
import com.cabbooking.services.BookingService;
import com.cabbooking.services.DriverService;
import com.cabbooking.utils.exceptions.BookingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BookingPanel extends JPanel {
    private final User usuario;
    private final MainFrame mainFrame;
    private JComboBox<Driver> driverCombo;
    private JTextField origenField;
    private JTextField destinoField;
    private JLabel precioLabel;

    public BookingPanel(User usuario, MainFrame mainFrame) {
        this.usuario = usuario;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        initUI();
    }

    private void initUI() {
        // Top Panel for inputs
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Origen Field
        JLabel origenLabel = new JLabel("Origen:");
        origenField = new JTextField(20);

        // Destino Field
        JLabel destinoLabel = new JLabel("Destino:");
        destinoField = new JTextField(20);

        // Driver Combo
        JLabel driverLabel = new JLabel("Conductor:");
        driverCombo = new JComboBox<>();

        // Precio Label
        precioLabel = new JLabel("0.00");
        JLabel precioTitleLabel = new JLabel("Precio estimado: $");

        // Layout for top panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(origenLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(origenField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(destinoLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(destinoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(driverLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(driverCombo, gbc);

        // Center Panel for price
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(precioTitleLabel);
        centerPanel.add(precioLabel);

        // Bottom Panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton calcularPrecioBtn = new JButton("Calcular Precio");
        calcularPrecioBtn.addActionListener(this::calcularPrecio);

        JButton reservarBtn = new JButton("Reservar");
        reservarBtn.addActionListener(this::reservarViaje);

        JButton historialBtn = new JButton("Historial");
        historialBtn.addActionListener(e -> mainFrame.mostrarHistorial(usuario.getId()));

        bottomPanel.add(calcularPrecioBtn);
        bottomPanel.add(reservarBtn);
        bottomPanel.add(historialBtn);

        // Map Panel
        JLabel mapLabel = new JLabel("Seleccione origen y destino");
        mapLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(mapLabel, BorderLayout.CENTER);

        // Load available drivers
        cargarConductoresDisponibles();
    }

    private void cargarConductoresDisponibles() {
        driverCombo.removeAllItems();
        List<Driver> conductores = DriverService.getConductoresDisponibles();
        conductores.forEach(driverCombo::addItem);
    }

    private void calcularPrecio(ActionEvent e) {
        String origen = origenField.getText().trim();
        String destino = destinoField.getText().trim();

        if (!origen.isEmpty() && !destino.isEmpty()) {
            double precio = calcularPrecio();
            precioLabel.setText(String.format("%.2f", precio));
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese origen y destino", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calcularPrecio() {
        int distancia = Math.max(origenField.getText().length(), 1) + Math.max(destinoField.getText().length(), 1);
        return distancia * 2.5; // Este es un cálculo temporal. Deberías considerar usar distancia real.
    }

    private void reservarViaje(ActionEvent e) {
        try {
            String origen = origenField.getText().trim();
            String destino = destinoField.getText().trim();

            if (origen.isEmpty() || destino.isEmpty()) {
                throw new BookingException("Debe completar origen y destino");
            }

            if (driverCombo.getSelectedItem() == null) {
                throw new BookingException("Seleccione un conductor disponible");
            }

            Driver conductor = (Driver) driverCombo.getSelectedItem();
            double precio = Double.parseDouble(precioLabel.getText());

            Ride nuevoViaje = new Ride(
                    usuario.getId(),
                    conductor.getId(),
                    origen,
                    destino,
                    precio
            );

            BookingService.createRide(nuevoViaje);
            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                    "¡Reserva exitosa!\nID del viaje: " + nuevoViaje.getId(),
                    "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (BookingException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error en reserva",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        origenField.setText("");
        destinoField.setText("");
        precioLabel.setText("0.00");
        cargarConductoresDisponibles();
    }
}
