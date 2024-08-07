package com.transfer.transferMoney.service;

import com.transfer.transferMoney.dto.TransferAdditionalInfoDTO;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService {
    Transfer saveTransfer(Transfer transfer, Boolean addContact);
    TransferDTO searchTransferById(Integer id);
    List<TransferAdditionalInfoDTO> getTransferDTOS(List<Transfer> transferList);

}
