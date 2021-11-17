package com.depromeet.threedollar.api.controller.user;

import com.depromeet.threedollar.api.controller.MockMvcUtils;
import com.depromeet.threedollar.api.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserMockApiCaller extends MockMvcUtils {

    public UserMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<LoginResponse> getTestToken() throws Exception {
        MockHttpServletRequestBuilder builder = get("/test-token");

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
