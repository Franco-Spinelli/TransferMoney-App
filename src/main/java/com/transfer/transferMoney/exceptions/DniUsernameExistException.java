package com.transfer.transferMoney.exceptions;

public class DniUsernameExistException extends RuntimeException{
    public DniUsernameExistException(String message) {
        super(message);
    }
}
