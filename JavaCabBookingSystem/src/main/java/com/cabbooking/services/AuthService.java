package com.cabbooking.services;

import com.cabbooking.models.User;
import com.cabbooking.models.Driver;
import com.cabbooking.enums.EstadoConductor;
import com.cabbooking.utils.exceptions.AuthException;
import com.cabbooking.utils.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private static final Map<String, User> usersDB = new HashMap<>();
    private static final UserService userService = new UserService();
    private static final String USER_FILE = "data/users.json";

    static {
        try {
            // Load users from JSON file
            List<User> users = JsonUtil.leer(USER_FILE, User.class);
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    usersDB.put(user.getEmail(), user);
                }
            }

            // If no users, add a default admin
            if (usersDB.isEmpty()) {
                String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
                User adminUser = new User("admin-id", "Administrador", "admin@example.com", hashedPassword, "ADMIN");
                usersDB.put(adminUser.getEmail(), adminUser);
                JsonUtil.escribir(USER_FILE, List.of(adminUser));
            }
        } catch (Exception e) {
            System.err.println("Error inicializando AuthService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static User login(String email, String password) throws AuthException {
        // Use UserService for validation
        if (!userService.validarUsuario(email, password)) {
            throw new AuthException("Correo o contraseña incorrectos");
        }

        // Retrieve user from usersDB or reload from file
        User user = usersDB.get(email);
        if (user == null) {
            // Reload users from file
            List<User> users = JsonUtil.leer(USER_FILE, User.class);
            if (users != null) {
                user = users.stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new AuthException("Usuario no encontrado"));
                usersDB.put(email, user);
            }
        }

        return user;
    }

    public static void register(User user) throws AuthException {
        userService.registrarUsuario(user);
        // Update in-memory database
        usersDB.put(user.getEmail(), user);
    }
    
    public static void registerDriver(User user, String vehiculo, String telefono) throws AuthException {
        try {
            // Validar datos de entrada
            if (user == null) {
                System.err.println("Error: Intento de registrar conductor con usuario nulo");
                throw new AuthException("Usuario no puede ser nulo");
            }
            
            if (vehiculo == null || vehiculo.trim().isEmpty()) {
                System.err.println("Error: Vehículo inválido para el conductor");
                throw new AuthException("Vehículo es obligatorio");
            }
            
            if (telefono == null || telefono.trim().isEmpty()) {
                System.err.println("Error: Teléfono inválido para el conductor");
                throw new AuthException("Teléfono es obligatorio");
            }

            // Primero registramos el usuario si no está registrado
            if (userService.obtenerUsuarioPorEmail(user.getEmail()).isEmpty()) {
                System.out.println("Registrando usuario antes de registrar como conductor: " + user.getEmail());
                register(user);
            }
            
            // Crear conductor con los datos proporcionados
            Driver driver = new Driver(
                user.getId(),  // Usar el ID del usuario
                user.getName(), 
                telefono, 
                vehiculo
            );
            driver.setEstado(EstadoConductor.DISPONIBLE);
            
            // Registra el conductor usando el servicio de conductores
            DriverService.registrarConductor(driver);
            
            System.out.println("Conductor registrado exitosamente: " + driver);
        } catch (Exception e) {
            System.err.println("Error al registrar conductor: " + e.getMessage());
            e.printStackTrace();
            throw new AuthException("Error al registrar como conductor: " + e.getMessage());
        }
    }
}
