package com.transfer.transferMoney.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.transfer.transferMoney.Repository.TransferRepository;
import com.transfer.transferMoney.dto.TransferAdditionalInfoDTO;
import com.transfer.transferMoney.dto.TransferDTO;
import com.transfer.transferMoney.model.Role;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.transfer.transferMoney.exceptions.AccountBalanceException;
import com.transfer.transferMoney.model.Transfer;
import com.transfer.transferMoney.model.User;
import com.transfer.transferMoney.service.TransferServiceImpl;
import com.transfer.transferMoney.service.UserService;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TransferServiceImpl underTest;
    private User user1;
    private User user2;
    private Transfer transfer;
    @BeforeEach
    void setUp() {

         user1 = User.builder()
                .id(1)
                .username("exampleUser")
                .email("example@gmail.com")
                .password("password")
                .firstname("John")
                .lastname("Doe")
                .dni(12345678L)
                .cbu(new BigInteger("1234567890123456789012"))
                .moneyAccount(new BigDecimal("1000.00"))
                .role(Role.USER)
                .receivedTransferList(new ArrayList<>())
                .myTransferList(new ArrayList<>())
                .build();
         user2 = User.builder()
                .id(2)
                .username("exampleUser2")
                .email("exampl2e@gmail.com")
                .password("password2")
                .firstname("Jack")
                .lastname("Dice")
                .dni(12345778L)
                .cbu(new BigInteger("12345678901234567847855"))
                .moneyAccount(new BigDecimal("1000.00"))
                .role(Role.USER)
                .receivedTransferList(new ArrayList<>())
                .myTransferList(new ArrayList<>())
                .build();
        transfer = Transfer.builder()
                .transfer_id(1)
                .recipientUser(user2)
                .originUser(user1)
                .transferDate(new Date())
                .transferAmount(new BigDecimal("200.00"))
                .build();
    }

    @Test
    void testIsTransferValid_ValidTransfer() {
        //GIVEN
        given(userService.findUserAuthenticated()).willReturn(user1);
        given(userService.findByUsername(anyString())).willReturn(user2);
        given(transferRepository.save(any(Transfer.class))).willReturn(transfer);
        //WHEN
        Transfer saveTransfer = underTest.saveTransfer(transfer);
        //THEN
        assertThat(saveTransfer).isNotNull();
        assertThat(underTest.isTransferValid(transfer)).isTrue();
    }
    @Test
    void testIsNotTransferValid_transferAmount() {
        // GIVEN
        Transfer invalidTransfer = Transfer.builder()
                .transfer_id(2)
                .recipientUser(user2)
                .originUser(user1)
                .transferDate(new Date())
                .transferAmount(new BigDecimal("10.00"))
                .build();
        given(userService.findUserAuthenticated()).willReturn(user1);

        // WHEN
        assertThrows(AccountBalanceException.class, () -> {
            underTest.saveTransfer(invalidTransfer);
        });

        // THEN
        verify(transferRepository, never()).save(any(Transfer.class));
    }
    @Test
    void testIsNotTransferValid_UserBalance() {

        // GIVEN
        user1.setMoneyAccount(new BigDecimal("0.00"));
        Transfer invalidTransfer = Transfer.builder()
                .transfer_id(2)
                .recipientUser(user2)
                .originUser(user1)
                .transferDate(new Date())
                .transferAmount(new BigDecimal("100.00"))
                .build();
        given(userService.findUserAuthenticated()).willReturn(user1);

        // WHEN
       assertThrows(AccountBalanceException.class, () -> {
            underTest.saveTransfer(invalidTransfer);
        });

        // THEN

        verify(transferRepository, never()).save(any(Transfer.class));

    }
    @Test
    void canSearchTransferById() {
        //GIVEN
        given(transferRepository.findById(transfer.getTransfer_id())).willReturn(Optional.of(transfer));
        //WHEN
        TransferDTO transferDTO = underTest.searchTransferById(transfer.getTransfer_id());
        //THEN
        assertThat(transferDTO).isNotNull();

    }
    @Test
    void searchInvalidTransferById() {
        //GIVEN
        given(transferRepository.findById(transfer.getTransfer_id())).willReturn(Optional.empty());
        //WHEN
        TransferDTO transferDTO = underTest.searchTransferById(transfer.getTransfer_id());
        //THEN
        assertThat(transferDTO).isNull();

    }
    @Test
    void canGetTransfersList() {
        //GIVEN
       List<Transfer> transferList = new ArrayList<>();
       transferList.add(transfer);
        //WHEN
        List<TransferAdditionalInfoDTO> transferDTOList = underTest.getTransferDTOS(transferList);
        //THEN
        assertThat(transferDTOList).isNotNull();

    }
}