package com.depromeet.threedollar.api.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

public abstract class MockMvcUtils {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    protected MockMvcUtils(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

}
