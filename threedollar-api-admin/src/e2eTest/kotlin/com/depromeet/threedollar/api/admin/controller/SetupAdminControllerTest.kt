package com.depromeet.threedollar.api.admin.controller

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

internal abstract class SetupAdminControllerTest : SetupControllerTest() {

    @Autowired
    protected lateinit var userRepository: UserRepository

    protected lateinit var token: String

    @BeforeEach
    protected fun setupToken() {
        val response = objectMapper.readValue(
            mockMvc.perform(get("/test-token"))
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<String>>() {}
        )
        token = response.data
    }

    protected fun cleanup() {
        userRepository.deleteAll()
    }

}
