package com.depromeet.threedollar.api.controller.advice;

import com.depromeet.threedollar.common.exception.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Handling 405 Method Not Allowed")
    @Test
    void method_not_allowed_handler_test() throws Exception {
        // given & when
        mockMvc.perform(post("/"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.resultCode").value(ErrorCode.METHOD_NOT_ALLOWED_EXCEPTION.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED_EXCEPTION.getMessage()));
    }

}
