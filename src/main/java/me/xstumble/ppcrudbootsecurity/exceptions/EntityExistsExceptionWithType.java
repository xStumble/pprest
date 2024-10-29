package me.xstumble.ppcrudbootsecurity.exceptions;

import jakarta.persistence.EntityExistsException;

public class EntityExistsExceptionWithType extends EntityExistsException {

    private final String type;

    public EntityExistsExceptionWithType(String message, String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
