package com.krab51.webapp.controllers.rest.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseControllerImpl implements BaseController {
    private final ObjectMapper objectMapper;

    @Autowired
    public BaseControllerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Error while parsing JSON: " + e.getMessage());
        }
    }
}