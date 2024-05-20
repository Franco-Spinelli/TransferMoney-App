package com.transfer.transferMoney.Repository;

import com.transfer.transferMoney.User.User;
import com.transfer.transferMoney.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.cbu = :cbu")
    Optional<User> findByCbu(BigInteger cbu);
    @Query("SELECT u FROM User u WHERE u.dni = :dni")
    Optional<User> searchByDni(Integer dni);
}
