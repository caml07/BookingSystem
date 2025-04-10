package com.cabbooking.models;

import com.cabbooking.enums.EstadoViaje;

public class Ride {
    private final String id;
    private String clienteId;
    private String conductorId;
    private String origen;
    private String destino;
    private double precio;
    private EstadoViaje estado;

    public Ride(String clienteId, String conductorId, String origen, String destino, double precio) {
        this.id = java.util.UUID.randomUUID().toString();
        this.clienteId = clienteId;
        this.conductorId = conductorId;
        this.origen = origen;
        this.destino = destino;
        this.precio = precio;
        this.estado = EstadoViaje.PENDIENTE;
    }

    public String getId() { return id; }
    public String getClienteId() { return clienteId; }
    public String getConductorId() { return conductorId; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getPrecio() { return precio; }
    public EstadoViaje getEstado() { return estado; }

    public void setEstado(EstadoViaje estado) { this.estado = estado; }
}