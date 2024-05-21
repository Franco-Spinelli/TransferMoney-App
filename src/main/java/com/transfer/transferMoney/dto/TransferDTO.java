package com.transfer.transferMoney.dto;

import com.transfer.transferMoney.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private Integer transfer_id;
    private String recipientUser;
    private BigInteger recipientCbu;
    private String originUser;
    private BigDecimal transferAmount;
}
