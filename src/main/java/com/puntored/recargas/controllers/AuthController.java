package com.puntored.recargas.controllers;

import java.util.List;

import com.puntored.recargas.model.Transaction;
import com.puntored.recargas.repository.TransactionRepository;
import com.puntored.recargas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AuthController(AuthService authService, TransactionRepository transactionRepository) {
        this.authService = authService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate() {
        try {
            String token = authService.authenticate();
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error de autenticación: " + e.getMessage());
        }
    }

    @GetMapping("/getSuppliers")
    public ResponseEntity<String> getSuppliers() {
        try {
            String token = authService.authenticate();
            String suppliers = authService.getSuppliers(token);
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyRecharge(@RequestBody RechargeRequest request) {
        System.out.println("Recibida solicitud de recarga: " + request);
        try {
            String token = authService.authenticate();
            String result = authService.buyRecharge(token, request.getCellPhone(), request.getValue(), request.getSupplierId());
            System.out.println("Recarga completada con éxito: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error en la compra: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error en la compra: " + e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("Transacciones recuperadas: " + transactions.size());
        return ResponseEntity.ok(transactions);
    }

    static class RechargeRequest {
        private String cellPhone;
        private int value;
        private String supplierId;

        // Getters and setters
        public String getCellPhone() { return cellPhone; }
        public void setCellPhone(String cellPhone) { this.cellPhone = cellPhone; }
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
        public String getSupplierId() { return supplierId; }
        public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    }
}