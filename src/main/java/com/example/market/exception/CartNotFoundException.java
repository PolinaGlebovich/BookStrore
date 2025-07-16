package com.example.market.exception;

public class CartNotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public CartNotFoundException(String messageKey, Object... args) {
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}