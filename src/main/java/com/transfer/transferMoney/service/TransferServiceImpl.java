package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService{
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserService userService;

    @Override
    public TransferDTO saveTransfer(Transfer transfer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find de user authenticated in  the moment
        String username = authentication.getName();
        UserDTO userOrigin = userService.findByUsername(username);
        UserDTO userRecipient = userService.findByUsername(transfer.getRecipientUser().getUsername());
        Transfer newTransfer = transferRepository.save(transfer);
        return new TransferDTO(newTransfer.getTransfer_id(), userRecipient.getUsername(),userOrigin.getUsername(),newTransfer.getTransferAmount());
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
