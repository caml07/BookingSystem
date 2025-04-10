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
        List<Driver> todosLosConductores = JsonUtil.leer(DRIVERS_FILE, Driver.class);
        List<Driver> conductoresDisponibles = todosLosConductores.stream()
            .filter(driver -> driver.getEstado() == EstadoConductor.DISPONIBLE)
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("Conductores disponibles: " + conductoresDisponibles.size());
        return conductoresDisponibles;
    }

    public static void registrarConductor(Driver driver) throws DriverException {
        System.out.println("Intentando registrar conductor: " + driver);
        
        if (driver == null) {
            System.err.println("Error: Intento de registrar conductor nulo");
            throw new DriverException("El conductor no puede ser nulo");
        }
        
        // Validaciones detalladas
        if (driver.getNombre() == null || driver.getNombre().isEmpty()) {
            System.err.println("Error: Nombre de conductor inválido");
            throw new DriverException("El nombre del conductor es obligatorio");
        }
        if (driver.getTelefono() == null || driver.getTelefono().isEmpty()) {
            System.err.println("Error: Teléfono de conductor inválido");
            throw new DriverException("El teléfono del conductor es obligatorio");
        }
        if (driver.getVehiculo() == null || driver.getVehiculo().isEmpty()) {
            System.err.println("Error: Vehículo de conductor inválido");
            throw new DriverException("El vehículo del conductor es obligatorio");
        }

        // Obtener lista de conductores
        List<Driver> drivers = JsonUtil.leer(DRIVERS_FILE, Driver.class);
        System.out.println("Conductores existentes: " + drivers.size());

        // Verificar si ya existe un conductor con este teléfono
        boolean telefonoExiste = drivers.stream()
            .anyMatch(d -> d.getTelefono().equals(driver.getTelefono()));
        
        if (telefonoExiste) {
            System.err.println("Error: Teléfono ya registrado - " + driver.getTelefono());
            throw new DriverException("Teléfono ya registrado");
        }

        // Agregar conductor
        drivers.add(driver);
        
        // Guardar conductores
        try {
            JsonUtil.escribir(DRIVERS_FILE, drivers);
            System.out.println("Conductor registrado con éxito: " + driver.getNombre());
            System.out.println("Total de conductores después del registro: " + drivers.size());
        } catch (Exception e) {
            System.err.println("Error al guardar conductor: " + e.getMessage());
            e.printStackTrace();
            throw new DriverException("Error al guardar conductor: " + e.getMessage());
        }
    }
}