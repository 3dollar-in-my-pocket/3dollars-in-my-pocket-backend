package com.depromeet.threedollar.admin.controller.admin

import com.depromeet.threedollar.admin.controller.ControllerTestUtils
import com.depromeet.threedollar.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.ErrorCode
import com.depromeet.threedollar.domain.domain.admin.AdminRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class AdminControllerTest(
    @Autowired
    private val adminRepository: AdminRepository
) : ControllerTestUtils() {

    @BeforeEach
    fun setUp() {
        super.setup()
    }

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        adminRepository.deleteAll()
    }

    @DisplayName("GET /admin/v1/admin/me 200 OK")
    @Test
    fun 관리자가_자신의_관리자_정보를_조회한다() {
        // when
        val response = objectMapper.readValue(
            mockMvc.perform(
                get("/admin/v1/admin/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            )
                .andExpect(status().isOk)
                .andDo(print())
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {}
        )

        // then
        assertThat(response.data.email).isEqualTo("test.admin@test.com")
        assertThat(response.data.name).isEqualTo("테스트 관리자")
    }

    @Test
    fun 잘못된_토큰인경우_401_에러가_발생한다() {
        // when
        val response = objectMapper.readValue(
            mockMvc.perform(
                get("/admin/v1/admin/me")
                    .header(HttpHeaders.AUTHORIZATION, "Wrong Token")
            )
                .andExpect(status().isUnauthorized)
                .andDo(print())
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {}
        )

        // then
        assertThat(response.resultCode).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.code)
        assertThat(response.message).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.message)
    }

    @DisplayName("GET /admin/v1/admin/me 401")
    @Test
    fun 토큰을_넘기지_않은경우_401_에러가_발생한다() {
        // when
        val response = objectMapper.readValue(
            mockMvc.perform(
                get("/admin/v1/admin/me")
            )
                .andExpect(status().isUnauthorized)
                .andDo(print())
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {}
        )

        // then
        assertThat(response.resultCode).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.code)
        assertThat(response.message).isEqualTo(ErrorCode.UNAUTHORIZED_EXCEPTION.message)
    }

}
