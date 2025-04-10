package com.cabbooking.services;

import com.cabbooking.models.User;
import com.cabbooking.utils.exceptions.AuthException;

import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private static final Map<String, User> usersDB = new HashMap<>();

    static {
        // Simulación de usuarios preexistentes en la base de datos
        String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
        usersDB.put("admin@example.com", new User("Administrador", "admin@example.com", hashedPassword, "ADMIN"));
    }

    public static User login(String email, String password) throws AuthException {
        User user = usersDB.get(email);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new AuthException("Correo o contraseña incorrectos");
        }
        return user;
    }

    public static void register(User user) throws AuthException {
        if (usersDB.containsKey(user.getEmail())) {
            throw new AuthException("El correo ya está registrado");
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        usersDB.put(user.getEmail(), user);
    }
}