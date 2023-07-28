package com.krab51.webapp.exceptions;

/**
 * Класс, являющийся основным для бизнес-исключений
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}