package com.transfer.transferMoney.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @PostMapping(value = "userTransfer")
    public String welcome(){
        return "Welcome for secure endpoint";
    }
}
