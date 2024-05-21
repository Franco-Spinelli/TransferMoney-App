package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


@Primary
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User searchByDni(Integer dni) {return userRepository.searchByDni(dni).orElse(null); }
    @Override
    public User searchByCbu(BigInteger cbu) {
        return userRepository.findByCbu(cbu).orElse(null);
    }

    @Override
    public User findByUsername(String username) {return userRepository.findByUsername(username).orElse(null);}

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
