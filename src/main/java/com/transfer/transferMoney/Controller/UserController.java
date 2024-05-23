package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(value = "userTransfer")
    public String welcome(){
        return "Welcome for secure endpoint";
    }
    @GetMapping("transfers")
    public ResponseEntity<?>transfers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Transfer>transferList = userService.findTransfersMadeByUserId(user.getId());
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

    @GetMapping("receivedTransfers")
    public ResponseEntity<?>transfersReceived(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Transfer>transferList = userService.findTransfersReceivedByUserId(user.getId());
        List<TransferDTO> transferDTOList = getTransferDTOS(transferList);
        return ResponseEntity.ok(transferDTOList);
    }
}
