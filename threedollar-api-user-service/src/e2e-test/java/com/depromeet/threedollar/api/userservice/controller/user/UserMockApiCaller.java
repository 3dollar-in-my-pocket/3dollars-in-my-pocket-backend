package com.depromeet.threedollar.api.userservice.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.nio.charset.StandardCharsets;

import org.springframework.test.web.servlet.MockMvc;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.controller.MockMvcUtils;
import com.depromeet.threedollar.api.userservice.service.auth.dto.response.LoginResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserMockApiCaller extends MockMvcUtils {

    public UserMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<LoginResponse> getTestToken() throws Exception {
        return objectMapper.readValue(
            mockMvc.perform(get("/test-token"))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
