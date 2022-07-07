package com.depromeet.threedollar.api.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

public abstract class MockMvcHelper {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    protected MockMvcHelper(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

}
