package com.example.market.exception;

public class ImageDeleteFailedException extends RuntimeException{
    private final String messageKey;
    private final Object[] args;

    public ImageDeleteFailedException(String messageKey, Object... args) {
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
