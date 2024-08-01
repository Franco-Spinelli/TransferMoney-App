package com.transfer.transferMoney.service;

import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;

import java.math.BigInteger;
import java.util.List;

public interface UserService {
    User searchByDni(Long dni);
    User searchByCbu(BigInteger cbu);
    User findByUsername(String username);
    List<Transfer> findTransfersMadeByUserId(Integer userId);
    List<Transfer> findTransfersReceivedByUserId(Integer userId);
    void save(User user);
    User findByCbu(BigInteger cbu);
    boolean existByDni(Long dni);
    boolean existByCbu(BigInteger cbu);
    boolean existById(Integer id);
    boolean existByUsername(String username);
    User findUserAuthenticated();
    List<String>getContacts();
    void deleteContact(String username);
    User findUser(String username, BigInteger cbu);
    UserDTO userToUserDto(User user);

}
