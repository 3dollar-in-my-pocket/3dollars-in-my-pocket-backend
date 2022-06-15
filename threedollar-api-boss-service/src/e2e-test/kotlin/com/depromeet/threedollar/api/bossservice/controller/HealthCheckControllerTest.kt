package com.depromeet.threedollar.api.bossservice.controller

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import com.depromeet.threedollar.api.bossservice.SetupControllerTest
import com.depromeet.threedollar.common.constants.VersionConstants

internal class HealthCheckControllerTest : SetupControllerTest() {

    @Test
    fun healthCheck() {
        this.mockMvc.perform(get("/ping"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").value("가슴속 3천원 사장님 API 서버"))
    }

    @Test
    fun versionCheck() {
        this.mockMvc.perform(get("/version"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").value(VersionConstants.VERSION))
    }

}
