package com.transfer.transferMoney.service;

import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;

import java.math.BigInteger;
import java.util.List;

public interface UserService {
    User searchByDni(Integer dni);
    User searchByCbu(BigInteger cbu);
    User findByUsername(String username);
    List<Transfer> findTransfersMadeByUserId(Integer userId);
    void save(User user);

}
