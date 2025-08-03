package com.interview.business.exception;

import java.util.function.Supplier;

public final class ExceptionSupplierFactory {

    public static Supplier<EntityNotFoundException> forEntityWithIdNotFound(String entityName, Long entityId) {
        return () -> new EntityNotFoundException("exception.message.entity.not.found", entityName, entityId);
    }
}
