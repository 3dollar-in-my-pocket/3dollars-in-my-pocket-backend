package com.depromeet.threedollar.api.adminservice.controller.commonservice.admin

import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request.AddAdminRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.request.UpdateMyAdminInfoRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.admin.dto.response.AdminInfoResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

internal class AdminControllerTest : SetupAdminControllerTest() {

    @DisplayName("GET /admin/v1/account/admin/me")
    @Nested
    inner class GetMyAdminInfoApiTest {

        @Test
        fun 관리자가_자신의_관리자_정보를_조회한다() {
            // when & then
            mockMvc.get("/v1/account/admin/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.email") { value("test.admin@test.com") }
                jsonPath("$.data.name") { value("테스트 관리자") }
            }
        }

        @Test
        fun 잘못된_토큰인경우_401_에러가_발생한다() {
            // when & then
            mockMvc.get("/v1/account/admin/me") {
                header(HttpHeaders.AUTHORIZATION, "Wrong Token")
            }.andDo {
                print()
            }.andExpect {
                status { isUnauthorized() }
                jsonPath("$.resultCode") { value(ErrorCode.UNAUTHORIZED.code) }
                jsonPath("$.message") { value(ErrorCode.UNAUTHORIZED.message) }
            }
        }

        @Test
        fun 토큰을_넘기지_않은경우_401_에러가_발생한다() {
            // when & then
            mockMvc.get("/v1/account/admin/me")
                .andDo {
                    print()
                }
                .andExpect {
                    status { isUnauthorized() }
                    jsonPath("$.resultCode") { value(ErrorCode.UNAUTHORIZED.code) }
                    jsonPath("$.message") { value(ErrorCode.UNAUTHORIZED.message) }
                }
        }

    }

    @DisplayName("PUT /admin/v1/account/admin/me")
    @Nested
    inner class UpdateMyAdminInfoApiTest {

        @Test
        fun 관리자가_자신의_관리자_정보를_수정한다() {
            // given
            val request = UpdateMyAdminInfoRequest(
                name = "변경 후 관리자 이름"
            )

            // when & then
            mockMvc.put("/v1/account/admin/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.email") { value("test.admin@test.com") }
                jsonPath("$.data.name") { value(request.name) }
            }
        }

    }

    @DisplayName("POST /admin/v1/account/admin")
    @Nested
    inner class RegisterAdminApiTest {

        @Test
        fun 관리자가_새로운_관리자를_등록한다() {
            // given
            val request = AddAdminRequest(
                email = "new@gmail.com",
                name = "새로운 관리자 이름"
            )

            // when & then
            mockMvc.post("/v1/account/admin") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
            }
        }

    }

    @DisplayName("GET /admin/v1/account/admins")
    @Nested
    inner class GetAdminInfos {

        @Test
        fun 등록된_관리자_목록을_조회한다() {
            // when & then
            mockMvc.get("/v1/account/admins") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                param("page", "1")
                param("size", "10")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.admins", hasSize<AdminInfoResponse>(1))
                jsonPath("$.data.admins[0].email") { value("test.admin@test.com") }
                jsonPath("$.data.admins[0].name") { value("테스트 관리자") }
            }
        }

    }

}
