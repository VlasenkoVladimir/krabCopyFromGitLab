package com.krab51.webapp.controllers.rest.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krab51.webapp.base.LoggerTest;
import com.krab51.webapp.dto.OperatorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public abstract class RestControllerTest<T> extends LoggerTest<T> {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected OperatorDto createOperatorDto() {
        OperatorDto operator = new OperatorDto();

        operator.id = 1L;
        operator.userName = "testOperator";

        return operator;
    }

    protected static ResultMatcher[] matchPage() {
        return new ResultMatcher[] {
                jsonPath("pageable", is("INSTANCE")),
                jsonPath("totalPages", is(1)),
                jsonPath("totalElements", is(2)),
                jsonPath("last", is(true)),
                jsonPath("numberOfElements", is(2)),
                jsonPath("number", is(0)),
                jsonPath("first", is(true)),
                jsonPath("size", is(2)),
                jsonPath("sort.unsorted", is(true)),
                jsonPath("sort.sorted", is(false)),
                jsonPath("sort.empty", is(true)),
                jsonPath("empty", is(false))
        };
    }
}