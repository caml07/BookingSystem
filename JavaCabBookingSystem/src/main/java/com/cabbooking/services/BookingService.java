package com.cabbooking.services;

import com.cabbooking.models.Ride;
import com.cabbooking.utils.JsonUtil;
import com.cabbooking.utils.exceptions.BookingException;

import java.util.List;

public class BookingService {
    private static final String RIDES_FILE = "data/rides.json";

    public static void createRide(Ride ride) throws BookingException {
        if (ride == null) {
            throw new BookingException("Datos del viaje inválidos");
        }

        if (ride.getClienteId() == null || ride.getConductorId() == null || ride.getOrigen() == null || ride.getDestino() == null) {
            throw new BookingException("Faltan datos esenciales para el viaje");
        }

        List<Ride> rides = JsonUtil.leer(RIDES_FILE, Ride.class);
        if (rides == null) {
            rides = new java.util.ArrayList<>();
        }
        rides.add(ride);
        JsonUtil.escribir(RIDES_FILE, rides);
    }

    public static List<Ride> getRidesByUser(String userId) {
        List<Ride> rides = JsonUtil.leer(RIDES_FILE, Ride.class);
        if (rides == null) {
            return new java.util.ArrayList<>();
        }
        return rides.stream()
                .filter(r -> r.getClienteId().equals(userId))
                .collect(java.util.stream.Collectors.toList());
    }

    public static void updateRideStatus(String rideId, String nuevoEstado) throws BookingException {
        List<Ride> rides = JsonUtil.leer(RIDES_FILE, Ride.class);
        if (rides == null) {
            throw new BookingException("No hay viajes registrados");
        }

        Ride rideToUpdate = rides.stream()
                .filter(r -> r.getId().equals(rideId))
                .findFirst()
                .orElseThrow(() -> new BookingException("Viaje no encontrado"));

        try {
            rideToUpdate.setEstado(com.cabbooking.enums.EstadoViaje.valueOf(nuevoEstado.toUpperCase()));
            JsonUtil.escribir(RIDES_FILE, rides);
        } catch (IllegalArgumentException e) {
            throw new BookingException("Estado de viaje no válido");
        }
    }
}