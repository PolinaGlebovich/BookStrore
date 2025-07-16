package com.example.market.exception;

public class BookNotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public BookNotFoundException(String messageKey, Object... args) {
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