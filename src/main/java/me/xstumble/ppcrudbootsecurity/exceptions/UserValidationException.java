package me.xstumble.ppcrudbootsecurity.exceptions;

import java.util.List;
import java.util.Map;

public class UserValidationException extends RuntimeException {

    private final Map<String, List<String>> errors;

    public UserValidationException(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

}
