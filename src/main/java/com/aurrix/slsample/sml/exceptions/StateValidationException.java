package com.aurrix.slsample.sml.exceptions;

import java.util.List;

public class StateValidationException extends RuntimeException {
    String message;
    List<?> errors;

    public StateValidationException(String message, List<?> errors) {
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }
}
