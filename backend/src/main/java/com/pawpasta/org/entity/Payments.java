package com.pawpasta.org.entity;


import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private double Distance;

    @Column(nullable = false)
    private String Source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false, precision = 18, scale = 2)
    private double amount;

    private String status;

    private String paymentCode;

    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private LocalDateTime creatAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;



}
