package com.puntored.recargas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cellPhone;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String supplierId;

    @Column(nullable = false)
    private String transactionalId;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public Transaction(String cellPhone, int amount, String supplierId, String transactionalId) {
        this.cellPhone = cellPhone;
        this.amount = amount;
        this.supplierId = supplierId;
        this.transactionalId = transactionalId;
        this.dateTime = LocalDateTime.now();
    }
}