package com.depromeet.threedollar.api.user.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        this.mockMvc.perform(get("/ping"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").value("가슴속 3천원 유저 API 서버"))
    }

}
