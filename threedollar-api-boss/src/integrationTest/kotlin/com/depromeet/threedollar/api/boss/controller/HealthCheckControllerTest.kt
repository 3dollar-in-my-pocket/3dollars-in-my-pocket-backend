package com.depromeet.threedollar.api.boss.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        this.mockMvc.perform(get("/ping"))
            .andExpect(status().isOk)
    }

}
