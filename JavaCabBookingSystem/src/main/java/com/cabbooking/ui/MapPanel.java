package com.cabbooking.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel {
    private Image mapImage;
    private Point origen;
    private Point destino;

    public MapPanel() {
        try {
            mapImage = new ImageIcon(getClass().getResource("/resources/mapa.png")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen del mapa");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (origen == null) {
                    origen = e.getPoint();
                } else if (destino == null) {
                    destino = e.getPoint();
                    // Aquí puedes implementar una lógica de cálculo de precio
                    JOptionPane.showMessageDialog(MapPanel.this, "Punto de destino seleccionado.");
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawString("Error al cargar el mapa", 20, 20);
        }

        if (origen != null) {
            g.setColor(Color.RED);
            g.fillOval(origen.x - 5, origen.y - 5, 10, 10);
        }

        if (destino != null) {
            g.setColor(Color.BLUE);
            g.fillOval(destino.x - 5, destino.y - 5, 10, 10);
        }
    }

    // Método para calcular el precio de la reserva, ejemplo simple de lógica
    public double calcularPrecio() {
        if (origen != null && destino != null) {
            int distancia = (int) origen.distance(destino);
            return distancia * 0.5; // Ejemplo de tarifa
        }
        return 0.0;
    }

    public Point getOrigen() {
        return origen;
    }

    public Point getDestino() {
        return destino;
    }
}
