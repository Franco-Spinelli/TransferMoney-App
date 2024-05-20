package com.transfer.transferMoney.Controller;

import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {
    @Autowired
    private TransferService transferService;
    @PostMapping("/create")
    public ResponseEntity<TransferDTO>create(@RequestBody Transfer transfer){
        TransferDTO transferDTO = transferService.saveTransfer(transfer);
        return ResponseEntity.ok(transferDTO);
    }
}
