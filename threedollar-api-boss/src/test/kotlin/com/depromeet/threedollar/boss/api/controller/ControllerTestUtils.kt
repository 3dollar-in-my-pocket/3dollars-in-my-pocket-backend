package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@AutoConfigureMockMvc
@SpringBootTest
abstract class ControllerTestUtils {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var bossAccountRepository: BossAccountRepository

    @Autowired
    protected lateinit var mockMvc: MockMvc

    protected lateinit var token: String

    @BeforeEach
    protected fun setupToken() {
        val response = objectMapper.readValue(
            mockMvc.perform(get("/test-token"))
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<LoginResponse>>() {}
        )
        token = response.data.token
    }

    protected fun cleanup() {
        bossAccountRepository.deleteAll()
    }

}
