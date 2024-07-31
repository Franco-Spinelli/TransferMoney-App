package com.transfer.transferMoney.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferService;
import com.transfer.transferMoney.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    /**
     * Creates a new transfer.
     * This method handles the creation of a new transfer based on the provided TransferDTO.
     * It checks for the existence of the recipient user by either username or CBU.
     * If the recipient user is found, their details are added to the TransferDTO.
     * If the recipient user is not found, an EntityNotFoundException is thrown.
     * The method also allows adding the recipient to contacts if specified.
     *
     * @param request A Map containing transfer details and a flag for adding the recipient to contacts.
     * @return A ResponseEntity containing the created TransferDTO.
     * @throws EntityNotFoundException if the recipient user is not found by username or CBU.
     */
    @PostMapping("/create")
    public ResponseEntity<TransferDTO> create(@RequestBody Map<String, Object> request) {
        ObjectMapper mapper = new ObjectMapper();
        TransferDTO transferDTO = mapper.convertValue(request.get("transferDTO"), TransferDTO.class);
        Boolean addContact = mapper.convertValue(request.get("addContact"), Boolean.class);
        User user;
        // Check if at least one of recipientUser or recipientCbu is provided
        if (transferDTO.getRecipientUser() != null) {
            // Find user by username
            user = userService.findByUsername(transferDTO.getRecipientUser());
            if (user == null) {
                throw new EntityNotFoundException("Recipient user not found with username: " + transferDTO.getRecipientUser());
            }
            transferDTO.setRecipientCbu(user.getCbu()); // Set CBU based on the found user
        } else if (transferDTO.getRecipientCbu() != null) {
            // Find user by CBU
            user = userService.findByCbu(transferDTO.getRecipientCbu());
            if (user == null) {
                throw new EntityNotFoundException("Recipient user not found with CBU: " + transferDTO.getRecipientCbu());
            }
            transferDTO.setRecipientUser(user.getUsername()); // Set username based on the found user
        } else {
            // If neither is provided, throw an exception
            throw new EntityNotFoundException("Recipient user or CBU must be provided.");
        }
        // Create a new transfer
        Transfer transfer = new Transfer(null, user, null, new Date(), transferDTO.getTransferAmount());
        Transfer newTransfer = transferService.saveTransfer(transfer,addContact);
        // Update TransferDTO with details from the saved transfer
        transferDTO.setOriginUser(newTransfer.getOriginUser().getUsername());
        transferDTO.setTransfer_id(newTransfer.getTransfer_id());
        transferDTO.setTransferDate(newTransfer.getTransferDate());
        // Return the created TransferDTO
        return ResponseEntity.ok(transferDTO);
    }

}
