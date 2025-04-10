package com.cabbooking.models;

import com.cabbooking.enums.EstadoConductor;

public class Driver {
    private final String id;
    private String nombre;
    private String telefono;
    private String vehiculo;
    private EstadoConductor estado;

    public Driver(String nombre, String telefono, String vehiculo) {
        this.id = java.util.UUID.randomUUID().toString();
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.estado = EstadoConductor.DISPONIBLE;
    }

    public Driver(String id, String nombre, String telefono, String vehiculo) {
        this.id = id != null ? id : java.util.UUID.randomUUID().toString();
        this.nombre = nombre;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.estado = EstadoConductor.DISPONIBLE;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getVehiculo() { return vehiculo; }
    public EstadoConductor getEstado() { return estado; }

    public void setEstado(EstadoConductor estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", nombre, vehiculo, telefono);
    }
}