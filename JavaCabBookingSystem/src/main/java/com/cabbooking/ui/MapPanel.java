package com.cabbooking.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class MapPanel extends JPanel {
    private Image mapImage;
    private Point origen;
    private Point destino;
    private boolean isOrigen = true;
    private JTextField origenField;
    private JTextField destinoField;

    public MapPanel(JTextField origenField, JTextField destinoField) {
        this.origenField = origenField;
        this.destinoField = destinoField;
        
        setBorder(BorderFactory.createTitledBorder("Seleccione origen y destino"));
        setPreferredSize(new Dimension(700, 400));
        
        loadMap();
        setupMouseListener();
    }
    
    public MapPanel() {
        this(new JTextField(), new JTextField());
    }
    
    private void loadMap() {
        try {
            // Try multiple methods to load the map
            mapImage = null;
            
            // Method 1: Using project folder path
            String imagePath = "src/main/resources/map.jpeg";
            File file = new File(imagePath);
            if (file.exists()) {
                mapImage = new ImageIcon(imagePath).getImage();
                System.out.println("Mapa cargado desde: " + file.getAbsolutePath());
            }
            
            // Method 2: Using class resource if Method 1 failed
            if (mapImage == null || mapImage.getWidth(null) <= 0) {
                URL resourceUrl = getClass().getResource("/map.jpeg");
                if (resourceUrl != null) {
                    mapImage = new ImageIcon(resourceUrl).getImage();
                    System.out.println("Mapa cargado desde recurso: " + resourceUrl);
                }
            }
            
            // If image still not loaded, create placeholder
            if (mapImage == null || mapImage.getWidth(null) <= 0) {
                System.err.println("ERROR: No se pudo cargar el mapa. Creando mapa de reemplazo.");
                mapImage = createPlaceholderMap();
            }
        } catch (Exception e) {
            System.err.println("ERROR al cargar mapa: " + e.getMessage());
            e.printStackTrace();
            mapImage = createPlaceholderMap();
        }
    }
    
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isOrigen) {
                    origen = e.getPoint();
                    origenField.setText("X:" + origen.x + ",Y:" + origen.y);
                    System.out.println("Origen seleccionado: " + origen);
                    isOrigen = false;
                } else {
                    destino = e.getPoint();
                    destinoField.setText("X:" + destino.x + ",Y:" + destino.y);
                    System.out.println("Destino seleccionado: " + destino);
                    isOrigen = true;
                }
                repaint();
            }
        });
    }

    private Image createPlaceholderMap() {
        BufferedImage placeholder = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        
        // White background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 800, 600);
        
        // Draw grid lines
        g2d.setColor(new Color(220, 220, 220));
        for (int i = 0; i < 800; i += 50) {
            g2d.drawLine(i, 0, i, 600);
        }
        for (int i = 0; i < 600; i += 50) {
            g2d.drawLine(0, i, 800, i);
        }
        
        // Draw road network
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 250, 800, 80);  // Horizontal road
        g2d.fillRect(350, 0, 80, 600);  // Vertical road
        
        // Draw locations
        g2d.setColor(new Color(255, 200, 200));
        g2d.fillRect(100, 100, 150, 100);  // Location 1
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillRect(550, 400, 150, 100);  // Location 2
        g2d.setColor(new Color(200, 255, 200));
        g2d.fillRect(550, 100, 150, 100);  // Location 3
        g2d.setColor(new Color(255, 255, 200));
        g2d.fillRect(100, 400, 150, 100);  // Location 4
        
        // Add text
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Centro Comercial", 110, 150);
        g2d.drawString("Parque Central", 560, 450);
        g2d.drawString("Universidad", 560, 150);
        g2d.drawString("Estacion de Tren", 110, 450);
        
        g2d.dispose();
        return placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw map
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        // Draw origen point
        if (origen != null) {
            g.setColor(Color.GREEN);
            g.fillOval(origen.x - 5, origen.y - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawOval(origen.x - 5, origen.y - 5, 10, 10);
            g.drawString("Origen", origen.x - 10, origen.y - 8);
        }
        
        // Draw destino point
        if (destino != null) {
            g.setColor(Color.RED);
            g.fillOval(destino.x - 5, destino.y - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawOval(destino.x - 5, destino.y - 5, 10, 10);
            g.drawString("Destino", destino.x - 10, destino.y - 8);
        }
    }

    public Point getOrigen() {
        return origen;
    }

    public Point getDestino() {
        return destino;
    }

    public void resetPoints() {
        origen = null;
        destino = null;
        isOrigen = true;
        if (origenField != null) origenField.setText("");
        if (destinoField != null) destinoField.setText("");
        repaint();
    }
}
