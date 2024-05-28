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
    @PostMapping("/create")
    public ResponseEntity<TransferDTO>create(@RequestBody TransferDTO transferDTO){
        User user = new User();
        if(transferDTO.getRecipientUser()==null){
            user = userService.findByCbu(transferDTO.getRecipientCbu());
            transferDTO.setRecipientUser(user.getUsername());
        }else if(transferDTO.getRecipientCbu()==null){
            user = userService.findByUsername(transferDTO.getRecipientUser());
            transferDTO.setRecipientCbu(user.getCbu());
        }else{
            throw new EntityNotFoundException("User o Cbu no exist");
        }
        Transfer transfer = new Transfer(null,user,null,new Date(),transferDTO.getTransferAmount());
        Transfer newtransfer = transferService.saveTransfer(transfer);
        transferDTO.setOriginUser(newtransfer.getOriginUser().getUsername());
        transferDTO.setTransfer_id(newtransfer.getTransfer_id());
        transferDTO.setTransferDate(newtransfer.getTransferDate());
        return ResponseEntity.ok(transferDTO);
    }
}
