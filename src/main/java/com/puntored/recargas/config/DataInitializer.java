package com.puntored.recargas.config;

import com.puntored.recargas.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthService authService;

    // Inyectar el servicio AuthService
    public DataInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Autenticar para obtener el token
        String token = authService.authenticate();

        // Realizar 3 recargas de prueba utilizando el método buyRecharge
        authService.buyRecharge(token, "3211234567", 1234, "8753");
        authService.buyRecharge(token, "3219876543", 10000, "9773");
        authService.buyRecharge(token, "3205556666", 15000, "3398");
    
        // Mensaje para confirmar que las recargas se han realizado
        System.out.println("Recargas de prueba realizadas exitosamente.");
    }
}
