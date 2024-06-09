package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.dto.TransferAdditionalInfoDTO;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.exceptions.AccountBalanceException;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserService userService;

    /**
     * Saves a transfer if the transfer amount is valid and the origin user has sufficient funds.
     *
     * @param transfer The transfer object containing transfer details.
     * @return The saved transfer object.
     * @throws AccountBalanceException if the origin user does not have sufficient funds or if the transfer amount is below the minimum threshold.
     */
    @Override
    public Transfer saveTransfer(Transfer transfer) {
        int numberResult = 0;
        BigDecimal minimumTransferAmount = new BigDecimal(100);
        // Find the authenticated user
        User userOrigin = userService.findUserAuthenticated();
        User userRecipient = userService.findByUsername(transfer.getRecipientUser().getUsername());
        transfer.setOriginUser(userOrigin);
            // Check if the transfer amount is above the minimum threshold and if the recipient is not the origin user
            if (transfer.getTransferAmount().compareTo(minimumTransferAmount) >= numberResult &&
                    !Objects.equals(transfer.getRecipientUser().getUsername(), userOrigin.getUsername()) &&
                    !Objects.equals(transfer.getRecipientUser().getCbu(), userOrigin.getCbu())) {

                // Check if the origin user has sufficient funds
                if (userOrigin.getMoneyAccount().compareTo(transfer.getTransferAmount()) >= numberResult) {
                    // Subtract the transfer amount from the origin user's account
                    userOrigin.setMoneyAccount(userOrigin.getMoneyAccount().subtract(transfer.getTransferAmount()));
                    userService.save(userOrigin);

                    // Add the transfer amount to the recipient user's account
                    userRecipient.setMoneyAccount(userRecipient.getMoneyAccount().add(transfer.getTransferAmount()));
                    userService.save(userRecipient);

                    // Save and return the transfer
                    return transferRepository.save(transfer);
                } else {
                    // Throw an exception if the origin user has insufficient funds
                    throw new AccountBalanceException("Insufficient funds");
                }
            } else {
                // Throw an exception if the transfer amount is below the minimum threshold or if the recipient is the same as the origin user
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

    @Override
    public List<TransferAdditionalInfoDTO> getTransferDTOS(List<Transfer> transferList) {
        List<TransferAdditionalInfoDTO>transferDTOList = new ArrayList<>();
        for (Transfer transfer : transferList){

            TransferAdditionalInfoDTO transferDTO = new TransferAdditionalInfoDTO(
                    transfer.getTransfer_id(),
                    transfer.getRecipientUser().getUsername(),
                    transfer.getRecipientUser().getCbu().toString(),
                    transfer.getOriginUser().getUsername(),
                    transfer.getTransferDate(),
                    transfer.getTransferAmount()
            );
            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }

}
