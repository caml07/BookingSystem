package com.cabbooking.services;

import com.cabbooking.models.Driver;
import com.cabbooking.utils.JsonUtil;
import com.cabbooking.utils.exceptions.DriverException;
import com.cabbooking.enums.EstadoConductor;

import java.util.List;
import java.util.stream.Collectors;

public class DriverService {
    private static final String DRIVERS_FILE = "data/drivers.json";

    public static List<Driver> getConductoresDisponibles() {
        List<Driver> drivers = JsonUtil.leer(DRIVERS_FILE, Driver.class);
        if (drivers == null) {
            return java.util.Collections.emptyList();
        }
        return drivers.stream()
                .filter(d -> d.getEstado().equals(EstadoConductor.DISPONIBLE))
                .collect(Collectors.toList());
    }

    public static void registrarConductor(Driver driver) throws DriverException {
        if (driver.getNombre() == null || driver.getNombre().isEmpty()) {
            throw new DriverException("El nombre del conductor es obligatorio");
        }
        if (driver.getTelefono() == null || driver.getTelefono().isEmpty()) {
            throw new DriverException("El teléfono del conductor es obligatorio");
        }
        if (driver.getVehiculo() == null || driver.getVehiculo().isEmpty()) {
            throw new DriverException("El vehículo del conductor es obligatorio");
        }

        List<Driver> drivers = JsonUtil.leer(DRIVERS_FILE, Driver.class);
        if (drivers == null) {
            drivers = new java.util.ArrayList<>();
        }

        if (drivers.stream().anyMatch(d -> d.getTelefono().equals(driver.getTelefono()))) {
            throw new DriverException("Teléfono ya registrado");
        }

        drivers.add(driver);
        JsonUtil.escribir(DRIVERS_FILE, drivers);
    }
}