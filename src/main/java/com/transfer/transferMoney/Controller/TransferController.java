package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferService;
import com.transfer.transferMoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<TransferDTO>create(@RequestBody TransferDTO transferDTO){
        User user = new User();
        if(transferDTO.getRecipientUser()==null){
            user = userService.findByCbu(transferDTO.getRecipientCbu());
        }else if(transferDTO.getRecipientCbu()==null){
            user = userService.findByUsername(transferDTO.getRecipientUser());
        }
        Transfer transfer = new Transfer(transferDTO.getTransfer_id(),user,null,transferDTO.getTransferAmount());
        Transfer newtransfer = transferService.saveTransfer(transfer);
        transferDTO.setOriginUser(newtransfer.getOriginUser().getUsername());
        transferDTO.setRecipientUser(newtransfer.getRecipientUser().getUsername());
        transferDTO.setRecipientCbu(newtransfer.getRecipientUser().getCbu());
        transferDTO.setTransfer_id(newtransfer.getTransfer_id());
        return ResponseEntity.ok(transferDTO);
    }
}
