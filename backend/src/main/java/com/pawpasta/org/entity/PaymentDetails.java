package com.pawpasta.org.entity;


import jakarta.persistence.*;

import lombok.*;

@Entity
@Table( name = "PaymentDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PaymentId", nullable = false)
    private Payments payments;

    private String bankCode;

    private String cardType;

    private String transactionNo;

    private String responseCode;

    @Lob
    private String secureHash;
}
