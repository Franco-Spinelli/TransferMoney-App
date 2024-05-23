package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.exceptions.AccountBalanceException;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService{
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserService userService;

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        int numberResult = 0;
        BigDecimal minimumTransferAmount = new BigDecimal(100);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User userOrigin = userService.findByUsername(username);
        User userRecipient = userService.findByUsername(transfer.getRecipientUser().getUsername());
        transfer.setOriginUser(userOrigin);

        if(transfer.getTransferAmount().compareTo(minimumTransferAmount)>=numberResult && !Objects.equals(transfer.getRecipientUser().getUsername(), userOrigin.getUsername()) && !Objects.equals(transfer.getRecipientUser().getCbu(), userOrigin.getCbu())) {
            if (userOrigin.getMoneyAccount().compareTo(transfer.getTransferAmount()) >= numberResult) {
                userOrigin.setMoneyAccount(userOrigin.getMoneyAccount().subtract(transfer.getTransferAmount()));
                userService.save(userOrigin);
                userRecipient.setMoneyAccount(userRecipient.getMoneyAccount().add(transfer.getTransferAmount()));
                userService.save(userRecipient);
                return  transferRepository.save(transfer);
            } else {
                throw new AccountBalanceException("Insufficient funds");
            }
        }else {
            throw new AccountBalanceException("The minimum transfer amount is " + minimumTransferAmount);
        }

    }

    @Override
    public TransferDTO searchTransferById(Integer id) {
        Optional<Transfer> optionalTransfer = transferRepository.findById(id);
        if(optionalTransfer.isEmpty()){
            return null;
        }
        Transfer newTransfer = optionalTransfer.get();
        return new TransferDTO(newTransfer.getTransfer_id(), newTransfer.getRecipientUser().getUsername(),newTransfer.getRecipientUser().getCbu(),newTransfer.getOriginUser().getUsername(),newTransfer.getTransferDate(),newTransfer.getTransferAmount());
    }

}
