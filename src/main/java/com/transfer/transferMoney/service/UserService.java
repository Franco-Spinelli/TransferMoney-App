package com.transfer.transferMoney.service;

import com.transfer.transferMoney.User.User;
import com.transfer.transferMoney.dto.UserDTO;

import java.math.BigInteger;
import java.util.Optional;

public interface UserService {
    UserDTO searchByDni(Integer dni);
    UserDTO searchByCbu(BigInteger cbu);
    UserDTO findByUsername(String username);
    User save(User user);

}
