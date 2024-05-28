package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.DepositDTO;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
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
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/me")
    public ResponseEntity<?>information(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(),user.getFirstname(),user.getLastname(),user.getDni(),user.getCbu());
        return ResponseEntity.ok(userDTO);
    }
    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositDTO depositDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        user.setMoneyAccount(user.getMoneyAccount().add(depositDTO.getMoneyToDeposit()));
        depositDTO.setDate(new Date());
        userService.save(user);
        return ResponseEntity.ok(String.format("Deposited $%s on %s", depositDTO.getMoneyToDeposit(), new SimpleDateFormat("MM/dd/yyyy").format(depositDTO.getDate())));
    }
    @GetMapping("/transfers")
    public ResponseEntity<?>transfers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Transfer>transferList = userService.findTransfersMadeByUserId(user.getId());
        List<TransferDTO> transferDTOList = getTransferDTOS(transferList);

        return ResponseEntity.ok(transferDTOList);
    }



    @GetMapping("/receivedTransfers")
    public ResponseEntity<?>transfersReceived(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Transfer>transferList = userService.findTransfersReceivedByUserId(user.getId());
        List<TransferDTO> transferDTOList = getTransferDTOS(transferList);
        return ResponseEntity.ok(transferDTOList);
    }

    private static List<TransferDTO> getTransferDTOS(List<Transfer> transferList) {
        List<TransferDTO>transferDTOList = new ArrayList<>();
        for (Transfer transfer : transferList){
            TransferDTO transferDTO = new TransferDTO(
                    transfer.getTransfer_id(),
                    transfer.getRecipientUser().getUsername(),
                    transfer.getRecipientUser().getCbu(),
                    transfer.getOriginUser().getUsername(),
                    transfer.getTransferDate(),
                    transfer.getTransferAmount()
            );
            transferDTOList.add(transferDTO);
        }
        return transferDTOList;
    }
}
