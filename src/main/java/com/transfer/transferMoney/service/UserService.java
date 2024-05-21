package com.transfer.transferMoney.service;

import com.transfer.transferMoney.model.User;

import java.math.BigInteger;

public interface UserService {
    User searchByDni(Integer dni);
    User searchByCbu(BigInteger cbu);
    User findByUsername(String username);
    void save(User user);

}
