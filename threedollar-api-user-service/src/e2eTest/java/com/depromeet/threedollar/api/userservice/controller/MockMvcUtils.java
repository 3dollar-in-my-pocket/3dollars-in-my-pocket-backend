package com.depromeet.threedollar.api.userservice.controller;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MockMvcUtils {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    protected MockMvcUtils(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

}
