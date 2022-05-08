package com.depromeet.threedollar.api.boss.config.interceptor

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.depromeet.threedollar.api.boss.controller.SetupBossAccountControllerTest
import com.depromeet.threedollar.common.exception.type.ErrorCode

internal class LoginCheckHandlerTest : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
    }

    @Test
    fun 로그인_테스트_로그인이_성공하면_200OK() {
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/boss/account/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "",
        "Bearer ",
        "Bearer",
        "wrong-token",
        "Bearer wrong-token"
    ])
    fun 로그인_테스트_토큰이_없거나_잘못된토큰인경우_401에러가_발생한다(token: String) {
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/boss/account/me")
            .header(HttpHeaders.AUTHORIZATION, token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.jsonPath("$.resultCode").value(ErrorCode.UNAUTHORIZED.code))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.UNAUTHORIZED.message))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty)
    }

}
