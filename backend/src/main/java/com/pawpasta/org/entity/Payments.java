package com.pawpasta.org.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "Payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Distance", nullable = false)
    private double distance;

    @Column(name = "Source", nullable = false)
    private String source;

    @Column(name = "Destination", nullable = false)
    private String destination;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "PaymentCode")
    private String paymentCode;

    @Column(name = "PaymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;
}
