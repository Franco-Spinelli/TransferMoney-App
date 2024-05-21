package com.transfer.transferMoney.Repository;

import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.cbu = :cbu")
    Optional<User> findByCbu(BigInteger cbu);
    @Query("SELECT u FROM User u WHERE u.dni = :dni")
    Optional<User> searchByDni(Integer dni);

    // Query method to find transfers made by a specific user
    @Query("SELECT t FROM Transfer t WHERE t.originUser.id = :userId")
    List<Transfer> findTransfersMadeByUserId(@Param("userId") Integer userId);
    // Query method to find transfers made by a specific user
    @Query("SELECT t FROM Transfer t WHERE t.recipientUser.id = :userId")
    List<Transfer> findTransfersReceivedByUserId(@Param("userId") Integer userId);


}
