# Cab Booking System

## Descripción
Sistema de reserva de taxis desarrollado en Java que permite a usuarios registrarse, iniciar sesión y reservar viajes con conductores disponibles.

## Características Principales
- Registro de usuarios y conductores
- Autenticación segura
- Selección de origen y destino en mapa
- Cálculo de precio de viaje
- Historial de viajes
- Gestión de conductores disponibles

## Tecnologías y Librerías
### Lenguaje
- Java 11+

### Librerías Principales
- **Swing**: Interfaz gráfica de usuario
- **Gson**: Serialización/deserialización JSON
- **BCrypt**: Hashing seguro de contraseñas

### Dependencias
- `com.google.code.gson:gson:2.8.9`
- `org.mindrot:jbcrypt:0.4`

## Estructura del Proyecto
```
src/main/java/com/cabbooking/
├── Main.java                 # Punto de entrada de la aplicación
├── models/                   # Clases de modelo de datos
│   ├── User.java
│   ├── Driver.java
│   └── Ride.java
├── services/                 # Lógica de negocio
│   ├── AuthService.java
│   ├── UserService.java
│   └── DriverService.java
├── ui/                       # Componentes de interfaz
│   ├── MainFrame.java
│   ├── LoginPanel.java
│   └── BookingPanel.java
└── utils/                    # Utilidades
    ├── JsonUtil.java
    └── exceptions/
```

## Configuración del Entorno
1. Instalar Java 11 o superior
2. Configurar IDE con soporte para Java
3. Importar dependencias de Gson y BCrypt

## Instalación
1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/JavaCabBookingSystem.git
```

2. Importar dependencias
   - Usar Maven o Gradle
   - O descargar manualmente los JARs de Gson y BCrypt

## Ejecución
```bash
java -jar CabBookingSystem.jar
```

## Funcionalidades
### Registro de Usuario
- Crear cuenta con email y contraseña
- Roles: Usuario y Conductor

### Reserva de Viaje
1. Seleccionar origen y destino en el mapa
2. Calcular precio automáticamente
3. Elegir conductor disponible
4. Confirmar reserva

## Seguridad
- Contraseñas hasheadas con BCrypt
- Validación de datos de entrada
- Manejo de excepciones

## Contribuciones
1. Hacer fork del repositorio
2. Crear rama de feature
3. Hacer commit de cambios
4. Crear Pull Request

## Licencia
MIT License

## Contacto
Eduardo - [Tu email]

## Capturas de Pantalla
[Espacio para agregar capturas de pantalla de la aplicación]

## Próximas Mejoras
- Integración con mapas reales
- Sistema de calificación de conductores
- Notificaciones push
- Soporte para pagos en línea
