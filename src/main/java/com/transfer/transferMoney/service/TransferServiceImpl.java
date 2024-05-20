package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService{
    @Autowired
    private TransferRepository transferRepository;

    @Override
    public TransferDTO saveTransfer(Transfer transfer) {
        Transfer newTransfer = transferRepository.save(transfer);
        return new TransferDTO(newTransfer.getTransfer_id(), newTransfer.getRecipientUser().getUsername(),newTransfer.getOriginUser().getUsername(),newTransfer.getTransferAmount());
    }

    @Override
    public TransferDTO searchTransferById(Integer id) {
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        if(optionalTransfer.isEmpty()){
            return null;
        }
        Transfer newTransfer = optionalTransfer.get();
        return new TransferDTO(newTransfer.getTransfer_id(), newTransfer.getRecipientUser().getUsername(),newTransfer.getOriginUser().getUsername(),newTransfer.getTransferAmount());
    }

}
