package com.depromeet.threedollar.admin.controller.admin

import com.depromeet.threedollar.admin.controller.ControllerTestUtils
import com.depromeet.threedollar.admin.service.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.user.domain.admin.AdminRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

internal class AdminControllerTest(
    private val adminRepository: AdminRepository
) : ControllerTestUtils() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        adminRepository.deleteAllInBatch()
    }

    @DisplayName("GET /admin/v1/account/admin/my-info 200 OK")
    @Test
    fun 관리자가_자신의_관리자_정보를_조회한다() {
        // when
        val response = objectMapper.readValue(mockMvc.get("/v1/account/admin/my-info") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {})

        // then
        assertAll({
            assertThat(response.data.email).isEqualTo("test.admin@test.com")
            assertThat(response.data.name).isEqualTo("테스트 관리자")
        })
    }

    @Test
    fun 잘못된_토큰인경우_401_에러가_발생한다() {
        // when
        val response = objectMapper.readValue(mockMvc.get("/v1/account/admin/my-info") {
            header(HttpHeaders.AUTHORIZATION, "Wrong Token")
        }.andExpect {
            status { isUnauthorized() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {})

        // then
        assertAll({
            assertThat(response.resultCode).isEqualTo(ErrorCode.UNAUTHORIZED.code)
            assertThat(response.message).isEqualTo(ErrorCode.UNAUTHORIZED.message)
        })
    }

    @DisplayName("GET /v1/account/admin/my-info 401")
    @Test
    fun 토큰을_넘기지_않은경우_401_에러가_발생한다() {
        // when
        val response = objectMapper.readValue(mockMvc.get("/v1/account/admin/my-info")
            .andExpect {
                status { isUnauthorized() }
            }.andDo {
                print()
            }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<AdminInfoResponse>>() {})

        // then
        assertAll({
            assertThat(response.resultCode).isEqualTo(ErrorCode.UNAUTHORIZED.code)
            assertThat(response.message).isEqualTo(ErrorCode.UNAUTHORIZED.message)
        })
    }

}
