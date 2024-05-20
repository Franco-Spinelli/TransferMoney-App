package com.transfer.transferMoney.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transfer_id;
    @ManyToOne
    @JoinColumn(name = "recipientUser_id")
    private User recipientUser;
    @ManyToOne
    @JoinColumn(name = "originUser_id")
    private User originUser;
    private BigDecimal transferAmount;
}
