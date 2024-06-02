package com.transfer.transferMoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdditionalInfoDTO {
    private Integer id;
    private  String username;
    private  String firstname;
    private  String lastname;
    private  Long dni;
    private String cbu;
    private BigDecimal moneyAccount;
    private String email;
}
