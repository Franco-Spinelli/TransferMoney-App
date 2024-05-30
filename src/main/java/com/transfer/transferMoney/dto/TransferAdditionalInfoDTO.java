package com.transfer.transferMoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferAdditionalInfoDTO {
    private Integer transfer_id;
    private String recipientUser;
    private String recipientCbu;
    private String originUser;
    private Date transferDate;
    private BigDecimal transferAmount;
}
