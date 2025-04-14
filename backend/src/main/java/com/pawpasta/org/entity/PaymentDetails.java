package com.pawpasta.org.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PaymentDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "PaymentId", referencedColumnName = "Id")
    private Payments payment;

    @Column(name = "BankCode")
    private String bankCode;

    @Column(name = "CardType")
    private String cardType;

    @Column(name = "TransactionNo")
    private String transactionNo;

    @Column(name = "ResponseCode")
    private String responseCode;

    @Column(name = "SecureHash", length = Integer.MAX_VALUE)
    private String secureHash;
}