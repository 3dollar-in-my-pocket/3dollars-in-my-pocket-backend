package com.depromeet.threedollar.api.userservice;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MockMvcHelper {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    protected MockMvcHelper(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

}
