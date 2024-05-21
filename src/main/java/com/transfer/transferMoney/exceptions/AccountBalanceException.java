package com.transfer.transferMoney.exceptions;

public class AccountBalanceException extends RuntimeException{
    public AccountBalanceException(String message) {
        super(message);
    }
}
