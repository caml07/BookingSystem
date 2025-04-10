package com.cabbooking.services;

import com.cabbooking.models.User;
import com.cabbooking.enums.RolUsuario;
import com.cabbooking.utils.JsonUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static final String USER_FILE = "data/users.json";

    public void registrarUsuario(User usuario) {
        List<User> usuarios = JsonUtil.leer(USER_FILE, User.class);
        if (usuarios == null) {
            usuarios = new java.util.ArrayList<>();
        }

        if (usuarios.stream().anyMatch(u -> u.getEmail().equals(usuario.getEmail()))) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
        usuario.setPassword(hashedPassword);

        usuarios.add(usuario);
        JsonUtil.escribir(USER_FILE, usuarios);
    }

    public Optional<User> obtenerUsuarioPorEmail(String email) {
        List<User> usuarios = JsonUtil.leer(USER_FILE, User.class);
        if (usuarios == null) {
            return Optional.empty();
        }
        return usuarios.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public boolean validarUsuario(String email, String password) {
        Optional<User> usuarioOpt = obtenerUsuarioPorEmail(email);
        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            return BCrypt.checkpw(password, usuario.getPassword());
        }
        return false;
    }

    public void actualizarRolUsuario(String email, RolUsuario rol) {
        List<User> usuarios = JsonUtil.leer(USER_FILE, User.class);
        if (usuarios == null) {
            return;
        }
        usuarios.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(user -> {
                    user.setRole(rol.name());
                    JsonUtil.escribir(USER_FILE, usuarios);
                });
    }
}