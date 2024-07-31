package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.dto.TransferAdditionalInfoDTO;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.exceptions.AccountBalanceException;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {
    private TransferRepository transferRepository;
    private UserService userService;

    /**
     * Saves a transfer if the transfer amount is valid and the origin user has sufficient funds.
     *
     * @param transfer The transfer object containing transfer details.
     * @return The saved transfer object.
     * @throws AccountBalanceException if the origin user does not have sufficient funds or if the transfer amount is below the minimum threshold.
     */
    @Override
    public Transfer saveTransfer(Transfer transfer, Boolean addContact) {
        // Check if the origin user has sufficient funds and other validation
        if (isTransferValid(transfer)) {
            // Find the authenticated user
            User userOrigin = userService.findUserAuthenticated();
            User userRecipient = userService.findByUsername(transfer.getRecipientUser().getUsername());
            transfer.setOriginUser(userOrigin);

            // Subtract the transfer amount from the origin user's account
            userOrigin.setMoneyAccount(userOrigin.getMoneyAccount().subtract(transfer.getTransferAmount()));
            userService.save(userOrigin);
            if(addContact){
                if (userOrigin.getContacts() == null) {
                    userOrigin.setContacts(new HashSet<>());
                }
                userOrigin.getContacts().add(userRecipient);
            }
            // Add the transfer amount to the recipient user's account
            userRecipient.setMoneyAccount(userRecipient.getMoneyAccount().add(transfer.getTransferAmount()));
            userService.save(userRecipient);

            // Save and return the transfer
            return transferRepository.save(transfer);
        }

        // This should never be reached because isTransferValid either returns true or throws an exception
        throw new IllegalStateException("Unexpected state: transfer validation failed but no exception thrown");
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
    public boolean isTransferValid(Transfer transfer){
        int numberResult = 0;
        BigDecimal minimumTransferAmount = new BigDecimal(100);
        // Find the authenticated user
        User userOrigin = userService.findUserAuthenticated();
        transfer.setOriginUser(userOrigin);
        // Check if the transfer amount is above the minimum threshold and if the recipient is not the origin user
        if (transfer.getTransferAmount().compareTo(minimumTransferAmount) < numberResult ||
                Objects.equals(transfer.getRecipientUser().getUsername(), userOrigin.getUsername()) ||
                Objects.equals(transfer.getRecipientUser().getCbu(), userOrigin.getCbu())) {
            throw new AccountBalanceException("The minimum transfer amount is " + minimumTransferAmount +
                    " or the recipient is the same as the origin user.");
        }
        // Check if the origin user has sufficient funds
        if (userOrigin.getMoneyAccount().compareTo(transfer.getTransferAmount()) < numberResult) {
            throw new AccountBalanceException("Insufficient funds");
        }
        return true;
    }

}
