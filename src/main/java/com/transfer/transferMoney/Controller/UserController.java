package com.transfer.transferMoney.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transfer.transferMoney.dto.*;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferService;
import com.transfer.transferMoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

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
        // Create a response map
        Map<String, String> response = new HashMap<>();
        response.put("message", String.format("Deposited $%s on %s", depositDTO.getMoneyToDeposit(),
                new SimpleDateFormat("MM/dd/yyyy").format(depositDTO.getDate())));
        // Return the response map
        return ResponseEntity.ok(response);
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
    /**
     * Endpoint to retrieve the list of contacts for the authenticated user.
     *
     * @return ResponseEntity with the list of the user's contacts.
     */
    @GetMapping("/get-contacts")
    public ResponseEntity<?> contacts() {
        return ResponseEntity.ok(userService.getContacts());
    }
    /**
     * Deletes a contact by its username.
     *
     * This method handles the deletion of a contact identified by the provided username.
     * It first checks if the contact exists using the userService.
     * If the contact does not exist, it returns a 404 Not Found response.
     * If the contact exists, it deletes the contact and returns a 200 OK response.
     *
     * @param username The username of the contact to be deleted.
     * @return A ResponseEntity with a status of 200 OK if the deletion is successful,
     *         or 404 Not Found if the contact does not exist.
     */
    @DeleteMapping("/delete-contact/{username}")
    public ResponseEntity<Void> deleteContact(@PathVariable String username) {
        if (!userService.existByUsername(username)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteContact(username);
        return ResponseEntity.ok().build();
    }
    /**
     * Handles GET requests to the "/get-user" endpoint.
     *
     * This method expects a request body containing a map with keys "cbu" and "username".
     * It retrieves the user based on these values. If the user is found, it converts the user
     * to a UserDTO and returns it with a 200 OK response. If the user is not found, it returns
     * a 404 Not Found response.
     *
     * @param request A map containing the keys "cbu" (BigInteger) and "username" (String).
     * @return ResponseEntity<?> containing the UserDTO if found, or a 404 Not Found response.
     */
    @PostMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestBody Map<String, Object> request) {
        ObjectMapper mapper = new ObjectMapper();
        BigInteger cbu = request.get("cbu") != null ? mapper.convertValue(request.get("cbu"), BigInteger.class) : null;
        String username = request.get("username") != null ? mapper.convertValue(request.get("username"), String.class) : null;
        User user = userService.findUser(username,cbu);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = userService.userToUserDto(user);
        return ResponseEntity.ok(userDTO);
    }
}
