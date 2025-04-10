package com.cabbooking.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> List<T> leer(String archivo, Class<T> tipo) {
        try {
            Path ruta;
            
            // Primero intentamos en el directorio del proyecto
            ruta = Paths.get("src/main/resources", archivo);
            
            // Si no existe ahí, buscamos en el classpath
            if (!Files.exists(ruta)) {
                URL resourceUrl = JsonUtil.class.getClassLoader().getResource(archivo);
                if (resourceUrl != null) {
                    try {
                        ruta = Paths.get(resourceUrl.toURI());
                    } catch (URISyntaxException e) {
                        // Si falla la conversión a URI, intentamos con el path del URL
                        String path = resourceUrl.getPath();
                        if (path.startsWith("/")) {
                            path = path.substring(1); // Quitar slash inicial en Windows
                        }
                        ruta = Paths.get(path);
                        System.err.println("Usando ruta alternativa: " + ruta);
                    }
                } else {
                    // Si no se encuentra el recurso, crear el archivo en resources
                    ruta = Paths.get("src/main/resources", archivo);
                }
            }

            // Crear directorios y archivo si no existen
            if (!Files.exists(ruta)) {
                Files.createDirectories(ruta.getParent());
                Files.createFile(ruta);
                return new ArrayList<>();
            }

            // Leer contenido del archivo
            String contenido = new String(Files.readAllBytes(ruta), StandardCharsets.UTF_8);
            
            // Si el archivo está vacío, devolver lista vacía
            if (contenido.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            Type tipoLista = TypeToken.getParameterized(List.class, tipo).getType();
            List<T> listaOriginal = GSON.fromJson(contenido, tipoLista);
            
            // Convertir a lista modificable si no lo es ya
            return listaOriginal instanceof ArrayList 
                ? listaOriginal 
                : new ArrayList<>(listaOriginal);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + archivo + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void escribir(String archivo, List<T> datos) {
        try {
            // Guardar en src/main/resources
            Path resourcePath = Paths.get("src", "main", "resources", archivo);
            guardarEnRuta(resourcePath, datos);
            
            // También guardar en target/classes para que se use en tiempo de ejecución
            Path targetPath = Paths.get("target", "classes", archivo);
            guardarEnRuta(targetPath, datos);
            
        } catch (IOException e) {
            System.err.println("Error crítico al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static <T> void guardarEnRuta(Path ruta, List<T> datos) throws IOException {
        System.out.println("Guardando archivo en: " + ruta.toAbsolutePath());
        
        // Crear directorios si no existen
        if (!Files.exists(ruta.getParent())) {
            Files.createDirectories(ruta.getParent());
            System.out.println("Se creó el directorio: " + ruta.getParent().toAbsolutePath());
        }
        
        // Convertir datos a JSON
        String jsonContent = GSON.toJson(datos);
        
        // Escribir el archivo
        Files.write(ruta, jsonContent.getBytes(StandardCharsets.UTF_8));
        System.out.println("Archivo guardado exitosamente en: " + ruta.toAbsolutePath());
        System.out.println("Contenido JSON guardado: " + jsonContent);
    }
}