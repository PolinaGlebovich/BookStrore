package com.example.market.exception;

public class CartItemNotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public CartItemNotFoundException(String messageKey, Object... args) {
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