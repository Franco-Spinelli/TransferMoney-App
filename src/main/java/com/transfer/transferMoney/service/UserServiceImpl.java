package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.User.User;
import com.transfer.transferMoney.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    @Override
    public UserDTO searchByDni(Integer dni) {
        Optional<User> user = userRepository.searchByDni(dni);
        return user.map(value -> new UserDTO(value.getId(), value.getUsername(), value.getFirstname(), value.getLastname(), value.getDni(), value.getCbu())).orElse(null);
    }
    @Override
    public UserDTO searchByCbu(BigInteger cbu) {
        Optional<User> user = userRepository.findByCbu(cbu);
        return user.map(value -> new UserDTO(value.getId(), value.getUsername(), value.getFirstname(), value.getLastname(), value.getDni(), value.getCbu())).orElse(null);

    }

    @Override
    public UserDTO findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> new UserDTO(value.getId(), value.getUsername(), value.getFirstname(), value.getLastname(), value.getDni(), value.getCbu())).orElse(null);

    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
