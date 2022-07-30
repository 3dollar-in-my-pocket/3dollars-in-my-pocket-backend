package com.depromeet.threedollar.api.adminservice

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.AdminRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

internal abstract class SetupAdminControllerTest : ControllerTest() {

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

}
