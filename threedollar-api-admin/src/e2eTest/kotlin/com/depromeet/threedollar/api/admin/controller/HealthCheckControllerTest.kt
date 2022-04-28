package com.depromeet.threedollar.api.admin.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        mockMvc.get("/ping")
            .andExpect {
                status { isOk() }
            }
    }

}
