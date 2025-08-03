package com.interview.business.exception;

import lombok.Getter;

@Getter
public class InvalidParameterException extends RuntimeException {
    private final String messageKey;
    private final Object[] messageArgs;

    public InvalidParameterException(String messageKey, Object... messageArgs) {
        super();
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}
