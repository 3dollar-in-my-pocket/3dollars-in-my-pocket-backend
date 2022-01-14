//package com.depromeet.threedollar.boss.api.controller.account
//
//import com.depromeet.threedollar.boss.api.controller.ControllerTestUtils
//import com.depromeet.threedollar.domain.boss.domain.account.BossAccountSocialType
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.http.HttpHeaders
//import org.springframework.test.web.servlet.get
//
//internal class BossAccountControllerTest : ControllerTestUtils() {
//
//    @AfterEach
//    fun cleanUp() {
//        super.cleanup()
//    }
//
//    @DisplayName("GET /api/v1/boss/account/me 200 OK")
//    @Test
//    fun 사장님의_자신의_계정_정보를_조회한다() {
//        // when & then
//        mockMvc.get("/api/v1/boss/account/me") {
//            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
//        }.andDo {
//            print()
//        }.andExpect {
//            status { isOk() }
//            content {
//                jsonPath("$.data.socialType") { value(BossAccountSocialType.NAVER.toString()) }
//                jsonPath("$.data.name") { value("테스트 계정") }
//            }
//        }
//    }
//
//    @DisplayName("GET /api/v1/boss/account/me 401")
//    @Test
//    fun 잘못된_토큰이면_401_에러_발생() {
//        // when & then
//        mockMvc.get("/api/v1/boss/account/me") {
//            header(HttpHeaders.AUTHORIZATION, "Wrong Token")
//        }.andDo {
//            print()
//        }.andExpect {
//            status { isUnauthorized() }
//        }
//    }
//
//}
