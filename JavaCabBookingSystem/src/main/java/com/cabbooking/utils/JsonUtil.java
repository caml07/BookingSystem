package com.cabbooking.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> List<T> leer(String archivo, Class<T> tipo) {
        try {
            Path ruta = Paths.get(archivo);
            if (!Files.exists(ruta)) {
                return Collections.emptyList();
            }

            Type listOfT = new TypeToken<List<T>>(){}.getType();
            return GSON.fromJson(Files.newBufferedReader(ruta), listOfT);
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static <T> void escribir(String archivo, List<T> datos) {
        try {
            Files.createDirectories(Paths.get(archivo).getParent());
            Files.write(Paths.get(archivo), GSON.toJson(datos).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}