package com.interview.business.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] messageArgs;

    public EntityNotFoundException(String messageKey, Object... messageArgs) {
        super();
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}
