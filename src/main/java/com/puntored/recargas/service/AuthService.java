package com.puntored.recargas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntored.recargas.model.Transaction;
import com.puntored.recargas.repository.TransactionRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class AuthService {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final String apiKey = "mtrQF6Q11eosqyQnkMY0JGFbGqcxVg5icvfVnX1ifIyWDvwGApJ8WUM8nHVrdSkN";
    private final String rawUrl = "https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api";
    private final String authUrl = rawUrl + "/auth";
    private final String suppliersUrl = rawUrl + "/getSuppliers";
    private final String buyUrl = rawUrl + "/buy";

    @Autowired
    public AuthService(TransactionRepository transactionRepository, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String authenticate() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Credenciales de prueba, unicamente para el propósito de la prueba técnica, en producción se deben manejar de forma segura con variables de entorno
        String body = "{\"user\": \"user0147\", \"password\": \"#3Q34Sh0NlDS\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(authUrl, request, AuthResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getToken();
        } else {
            throw new RuntimeException("Error de autenticación: " + response.getStatusCode());
        }
    }

    public String getSuppliers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("x-api-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                suppliersUrl,
                HttpMethod.GET,
                entity,
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al obtener proveedores: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener proveedores: " + e.getMessage(), e);
        }
    }

    public String buyRecharge(String token, String cellPhone, int amount, String supplierId) {
        System.out.println("Iniciando compra de recarga: " + cellPhone + ", " + amount + ", " + supplierId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        String body = String.format("{\"cellPhone\": \"%s\", \"value\": \"%d\", \"supplierId\": \"%s\"}",
                                    cellPhone, amount, supplierId);
    
        HttpEntity<String> request = new HttpEntity<>(body, headers);
    
        System.out.println("Enviando solicitud a la API: " + body);
        ResponseEntity<String> response = restTemplate.postForEntity(buyUrl, request, String.class);
    
        System.out.println("Respuesta recibida: " + response.getStatusCode() + " - " + response.getBody());
    
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                String transactionalId = (String) responseMap.get("transactionalID");

                System.out.println("TransactionalID obtenido: " + transactionalId);

                Transaction transaction = new Transaction(cellPhone, amount, supplierId, transactionalId);
                Transaction savedTransaction = transactionRepository.save(transaction);

                System.out.println("Transacción guardada: " + savedTransaction);

                return response.getBody();
            } catch (Exception e) {
                System.err.println("Error al procesar la respuesta: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error al procesar la respuesta: " + e.getMessage(), e);
            }
        } else {
            System.err.println("Error en la compra: " + response.getStatusCode());
            throw new RuntimeException("Error en la compra: " + response.getStatusCode());
        }
    }

    @Data
    static class AuthResponse {
        private String token;
    }
}