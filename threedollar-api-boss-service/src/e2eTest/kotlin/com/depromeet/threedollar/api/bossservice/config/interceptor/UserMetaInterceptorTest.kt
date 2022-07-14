package com.depromeet.threedollar.api.bossservice.config.interceptor

import com.depromeet.threedollar.api.bossservice.SetupControllerTest
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.OsPlatformType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class UserMetaInterceptorTest : SetupControllerTest() {

    @DisplayName("IPhone 1.0.0")
    @Test
    fun IOS_디바이스_정보를_가져온다_1() {
        // given
        val userAgent = "1.0.0 (com.macgongmon.-dollar-in-my-pocket-manager-dev; build:7; iOS 15.5.0)"
        val traceId = "Root=1-62b5d6a4-06a5fc494921c46949dc2355"

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/test-device")
            .header(HttpHeaders.USER_AGENT, userAgent)
            .header("X-Amzn-Trace-Id", traceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.traceId").value(traceId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.osPlatform").value(OsPlatformType.IPHONE.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.applicationType").value(ApplicationType.BOSS_API.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.appVersion").value("1.0.0"))
    }

    @DisplayName("IPhone 1.0.1")
    @Test
    fun IOS_디바이스_정보를_가져온다_2() {
        // given
        val userAgent = "1.0.1 (com.macgongmon.-dollar-in-my-pocket-manager-dev; build:7; iOS 15.5.0)"
        val traceId = "traceId"

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/test-device")
            .header(HttpHeaders.USER_AGENT, userAgent)
            .header("X-Amzn-Trace-Id", traceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.traceId").value(traceId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.osPlatform").value(OsPlatformType.IPHONE.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.applicationType").value(ApplicationType.BOSS_API.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.appVersion").value("1.0.1"))
    }

    @DisplayName("Unknown Device")
    @Test
    fun UNKNOWN_디바이스() {
        // given
        val userAgent = "Unknown"
        val traceId = "Root=1-62b5d6a4-06a5fc494921c46949dc2355"

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/test-device")
            .header(HttpHeaders.USER_AGENT, userAgent)
            .header("X-Amzn-Trace-Id", traceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.traceId").value(traceId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.osPlatform").value(OsPlatformType.UNKNOWN.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.applicationType").value(ApplicationType.BOSS_API.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.appVersion").isEmpty)
    }

}
