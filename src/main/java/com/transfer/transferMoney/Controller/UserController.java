package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.*;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferService;
import com.transfer.transferMoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransferService transferService;
    /**
     * Retrieves information about the authenticated user.
     *
     * @return A ResponseEntity containing the UserAdditionalInfoDTO representing the authenticated user's details.
     */
    @GetMapping("/me")
    public ResponseEntity<?> information() {
        // Find the authenticated user
        User user = userService.findUserAuthenticated();
        // Convert user details to DTO
        UserAdditionalInfoDTO userDTO = new UserAdditionalInfoDTO(user.getId(), user.getUsername(), user.getFirstname(),
                user.getLastname(), user.getDni(), user.getCbu().toString(), user.getMoneyAccount(), user.getEmail());
        // Return the user DTO
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Deposits money into the authenticated user's account.
     *
     * @param depositDTO The DepositDTO containing details of the deposit.
     * @return A ResponseEntity indicating the success of the deposit operation.
     */
    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositDTO depositDTO) {
        // Find the authenticated user
        User user = userService.findUserAuthenticated();
        // Update the user's money account with the deposited amount
        user.setMoneyAccount(user.getMoneyAccount().add(depositDTO.getMoneyToDeposit()));
        // Update the deposit date
        depositDTO.setDate(new Date());
        // Save the updated user
        userService.save(user);
        // Return a success message
        return ResponseEntity.ok(String.format("Deposited $%s on %s", depositDTO.getMoneyToDeposit(),
                new SimpleDateFormat("MM/dd/yyyy").format(depositDTO.getDate())));
    }

    /**
     * Retrieves transfers made by the authenticated user.
     *
     * @return A ResponseEntity containing a list of TransferAdditionalInfoDTO representing transfer details.
     */
    @GetMapping("/transfers")
    public ResponseEntity<?> transfers() {
        // Find the authenticated user
        User user = userService.findUserAuthenticated();
        // Retrieve transfers made by the user
        List<Transfer> transferList = userService.findTransfersMadeByUserId(user.getId());
        // Convert transfers to DTOs
        List<TransferAdditionalInfoDTO> transferDTOList = transferService.getTransferDTOS(transferList);
        // Return the transfer DTOs
        return ResponseEntity.ok(transferDTOList);
    }

    /**
     * Retrieves transfers received by the authenticated user.
     *
     * @return A ResponseEntity containing a list of TransferAdditionalInfoDTO representing received transfer details.
     */
    @GetMapping("/receivedTransfers")
    public ResponseEntity<?> transfersReceived() {
        // Find the authenticated user
        User user = userService.findUserAuthenticated();
        // Retrieve transfers received by the user
        List<Transfer> transferList = userService.findTransfersReceivedByUserId(user.getId());
        // Convert transfers to DTOs
        List<TransferAdditionalInfoDTO> transferDTOList = transferService.getTransferDTOS(transferList);
        // Return the transfer DTOs
        return ResponseEntity.ok(transferDTOList);
    }


}
