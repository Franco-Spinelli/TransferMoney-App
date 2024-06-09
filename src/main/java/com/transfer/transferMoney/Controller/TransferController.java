package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferService;
import com.transfer.transferMoney.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    /**
     * Creates a new transfer.
     *
     * @param transferDTO The TransferDTO containing transfer details.
     * @return A ResponseEntity containing the created TransferDTO.
     * @throws EntityNotFoundException if the recipient user is not found by username or CBU.
     */
    @PostMapping("/create")
    public ResponseEntity<TransferDTO> create(@RequestBody TransferDTO transferDTO) {
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
        Transfer newTransfer = transferService.saveTransfer(transfer);
        // Update TransferDTO with details from the saved transfer
        transferDTO.setOriginUser(newTransfer.getOriginUser().getUsername());
        transferDTO.setTransfer_id(newTransfer.getTransfer_id());
        transferDTO.setTransferDate(newTransfer.getTransferDate());
        // Return the created TransferDTO
        return ResponseEntity.ok(transferDTO);
    }

}
