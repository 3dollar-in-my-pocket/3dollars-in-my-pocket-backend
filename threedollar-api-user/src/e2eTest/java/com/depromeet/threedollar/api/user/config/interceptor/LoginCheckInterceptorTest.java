package com.depromeet.threedollar.api.user.config.interceptor;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;

class LoginCheckInterceptorTest extends SetupUserControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @Test
    void 로그인_테스트_로그인이_성공하면_200OK() throws Exception {
        // when & then
        mockMvc.perform(get("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, token)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.userId").value(user.getId()))
            .andExpect(jsonPath("$.data.name").value(user.getName()))
            .andExpect(jsonPath("$.data.socialType").value(user.getSocialType().name()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "wrong-token"
    })
    void 로그인_테스트_토큰이_없는경우_401에러가_발생한다() throws Exception {
        // when & then
        mockMvc.perform(get("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "")
            )
            .andDo(print())
            .andExpect(jsonPath("$.resultCode").value(UNAUTHORIZED.getCode()))
            .andExpect(jsonPath("$.message").value(UNAUTHORIZED.getMessage()))
            .andExpect(jsonPath("$.data").isEmpty());
    }

}