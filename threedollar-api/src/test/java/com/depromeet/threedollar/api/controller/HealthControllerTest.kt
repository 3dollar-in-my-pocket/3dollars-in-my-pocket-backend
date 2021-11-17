package com.depromeet.threedollar.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class HealthControllerTest(
    private val mockMvc: MockMvc
) {

    @Test
    fun healthCheck() {
        this.mockMvc.perform(get("/ping"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data").value(ApiResponse.SUCCESS.data))
            .andExpect(jsonPath("$.resultCode").isEmpty)
            .andExpect(jsonPath("$.message").isEmpty)
    }

}
