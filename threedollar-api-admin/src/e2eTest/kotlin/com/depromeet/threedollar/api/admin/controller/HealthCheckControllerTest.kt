package com.depromeet.threedollar.api.admin.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        mockMvc.get("/ping")
            .andExpect {
                status { isOk() }

                jsonPath("$.data") { value("가슴속 삼천원 관리자 API") }
            }
    }

}
