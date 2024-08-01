package com.transfer.transferMoney.service;

import com.transfer.transferMoney.Repository.UserRepository;
import com.transfer.transferMoney.dto.UserDTO;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@Primary
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public User searchByDni(Long dni) {return userRepository.findByDni(dni).orElse(null); }
    @Override
    public User searchByCbu(BigInteger cbu) {
        return userRepository.findByCbu(cbu).orElse(null);
    }

    @Override
    public User findByUsername(String username) {return userRepository.findByUsername(username).orElse(null);}

    @Override
    public List<Transfer> findTransfersMadeByUserId(Integer userId) {
        return userRepository.findTransfersMadeByUserId(userId);
    }

    @Override
    public List<Transfer> findTransfersReceivedByUserId(Integer userId) {
        return userRepository.findTransfersReceivedByUserId(userId);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByCbu(BigInteger cbu) {
        return userRepository.findByCbu(cbu).orElse(null);
    }

    @Override
    public boolean existByDni(Long dni) {
        return userRepository.findByDni(dni).isPresent();
    }

    @Override
    public boolean existByCbu(BigInteger cbu) {
        return userRepository.findByCbu(cbu).isPresent();
    }

    @Override
    public boolean existById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existByUsername(String username) {
        return  userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User findUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Find user authenticated in  the moment
        String username = authentication.getName();
        return findByUsername(username);
    }

    @Override
    public List<String> getContacts() {
        User user = findUserAuthenticated();
        return user.getContacts().stream()
                .map(User::getUsername)
                .toList();
    }

    @Override
    public void deleteContact(String username) {
        User user = findUserAuthenticated();
        user.getContacts().removeIf(contact -> contact.getUsername().equals(username));
        save(user);
    }

    @Override
    public User findUser(String username, BigInteger cbu) {
        User user;
        if(existByUsername(username)){
            user = findByUsername(username);
        } else if (existByCbu(cbu)) {
            user = findByCbu(cbu);
        }else{
            throw new EntityNotFoundException();
        }
        return user;
    }

    @Override
    public UserDTO userToUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .dni(user.getDni())
                .cbu(user.getCbu())
                .build();
    }
}
