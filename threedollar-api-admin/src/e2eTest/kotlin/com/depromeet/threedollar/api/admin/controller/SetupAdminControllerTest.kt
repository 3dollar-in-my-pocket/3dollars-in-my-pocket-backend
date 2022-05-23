package com.depromeet.threedollar.api.admin.controller

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.rds.vendor.domain.admin.AdminRepository
import com.fasterxml.jackson.core.type.TypeReference

internal abstract class SetupAdminControllerTest : SetupControllerTest() {

    @Autowired
    protected lateinit var adminRepository: AdminRepository

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
        adminRepository.deleteAll()
    }

}
