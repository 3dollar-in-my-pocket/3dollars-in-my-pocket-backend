package com.depromeet.threedollar.api.admin.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.depromeet.threedollar.common.constants.VersionConstants

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        mockMvc.get("/ping")
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value("가슴속 삼천원 관리자 API") }
            }
    }

    @Test
    fun versionCheck() {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/version"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(VersionConstants.VERSION))
    }

}
