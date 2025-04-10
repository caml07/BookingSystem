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
    private MapPanel mapPanel;

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
        topPanel.add(origenLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(origenField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        topPanel.add(destinoLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(destinoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(driverLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(driverCombo, gbc);

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

        // Map Panel con selección de origen y destino
        mapPanel = new MapPanel(origenField, destinoField);
        mapPanel.setPreferredSize(new Dimension(600, 400));

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(mapPanel, BorderLayout.CENTER);

        // Load available drivers
        cargarConductoresDisponibles();
    }

    private void cargarConductoresDisponibles() {
        driverCombo.removeAllItems();
        List<Driver> conductores = DriverService.getConductoresDisponibles();
        System.out.println("Conductores cargados: " + conductores.size());
        if (conductores.isEmpty()) {
            System.out.println("No hay conductores disponibles");
        } else {
            for (Driver driver : conductores) {
                driverCombo.addItem(driver);
                System.out.println("Conductor agregado: " + driver);
            }
        }
    }

    private void calcularPrecio(ActionEvent e) {
        Point origen = mapPanel.getOrigen();
        Point destino = mapPanel.getDestino();

        if (origen != null && destino != null) {
            double precio = calcularPrecioViaje();
            precioLabel.setText(String.format("%.2f", precio));
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione origen y destino en el mapa", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calcularPrecioViaje() {
        Point origen = mapPanel.getOrigen();
        Point destino = mapPanel.getDestino();
        
        if (origen == null || destino == null) {
            return 10.0; // Precio mínimo si no se seleccionaron puntos
        }
        
        // Calcular distancia euclidiana entre puntos
        double distancia = Math.sqrt(
            Math.pow(destino.x - origen.x, 2) + 
            Math.pow(destino.y - origen.y, 2)
        );
        
        // Mostrar la distancia en píxeles
        System.out.println("Distancia en píxeles: " + distancia);
        
        // Convertir a kilómetros (asumiendo que 100 píxeles = 1 kilómetro)
        double distanciaKm = distancia / 100.0;
        System.out.println("Distancia en km: " + distanciaKm);
        
        // Tarifa base + precio por kilómetro (más realista: tarifa base $5 + $2.5 por km)
        double precio = 5.0 + (distanciaKm * 2.5);
        System.out.println("Precio calculado: $" + precio);
        
        return precio;
    }

    private void reservarViaje(ActionEvent e) {
        try {
            Point origen = mapPanel.getOrigen();
            Point destino = mapPanel.getDestino();

            if (origen == null || destino == null) {
                throw new BookingException("Debe seleccionar origen y destino en el mapa");
            }

            if (driverCombo.getSelectedItem() == null) {
                throw new BookingException("Seleccione un conductor disponible");
            }

            Driver conductor = (Driver) driverCombo.getSelectedItem();
            // Calcular el precio actual (en lugar de leerlo del label que podría estar vacío)
        double precio = calcularPrecioViaje();
        precioLabel.setText(String.format("%.2f", precio));

            // Convertir puntos a cadenas para almacenamiento
            String origenStr = "X:" + origen.x + ",Y:" + origen.y;
            String destinoStr = "X:" + destino.x + ",Y:" + destino.y;
            
            Ride nuevoViaje = new Ride(
                    usuario.getId(),
                    conductor.getId(),
                    origenStr,
                    destinoStr,
                    precio
            );

            BookingService.createRide(nuevoViaje);
            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                "¡Reserva exitosa!\n" +
                "ID del viaje: " + nuevoViaje.getId() + "\n" +
                "Conductor: " + conductor.getNombre() + "\n" +
                "Vehículo: " + conductor.getVehiculo() + "\n" +
                "Origen: " + origenStr + "\n" +
                "Destino: " + destinoStr + "\n" +
                "Precio: $" + String.format("%.2f", precio),
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
        mapPanel.resetPoints();
        cargarConductoresDisponibles();
    }
}
