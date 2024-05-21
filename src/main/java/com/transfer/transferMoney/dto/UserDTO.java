package com.transfer.transferMoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private  String username;
    private  String firstname;
    private  String lastname;
    private BigDecimal moneyAccount;
    private  Long dni;
    private BigInteger cbu;
}
