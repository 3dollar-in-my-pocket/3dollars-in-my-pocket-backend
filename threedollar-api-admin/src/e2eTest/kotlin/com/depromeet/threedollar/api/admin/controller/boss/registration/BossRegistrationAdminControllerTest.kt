package com.depromeet.threedollar.api.admin.controller.boss.registration

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.response.BossAccountRegistrationStoreResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator

internal class BossRegistrationAdminControllerTest(
    private val bossRegistrationRepository: BossRegistrationRepository
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossRegistrationRepository.deleteAll()
    }

    @DisplayName("PUT /v1/boss/account/registration/{registration.id}/reject")
    @Test
    fun `사장님 계정 가입 신청을 승인합니다`() {
        // given
        val registration = RegistrationCreator.create("social-id", BossAccountSocialType.GOOGLE)
        bossRegistrationRepository.save(registration)

        // when & then
        mockMvc.put("/v1/boss/account/registration/${registration.id}/apply") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
    }

    @DisplayName("PUT /v1/boss/account/registration/{registration.id}/reject")
    @Test
    fun `사장님 계정 가입 신청을 반려합니다`() {
        // given
        val registration = RegistrationCreator.create("social-id", BossAccountSocialType.GOOGLE)
        bossRegistrationRepository.save(registration)

        // when & then
        mockMvc.put("/v1/boss/account/registration/${registration.id}/reject") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
    }

    @DisplayName("GET /v1/boss/account/registrations")
    @Test
    fun `사장님 계정의 가입 신청 목록을 조회합니다`() {
        // given
        val registration = RegistrationCreator.create("social-id", BossAccountSocialType.GOOGLE)
        bossRegistrationRepository.save(registration)

        // when & then
        mockMvc.get("/v1/boss/account/registrations") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", hasSize<BossAccountRegistrationResponse>(1))
                jsonPath("$.data[0].registrationId") { value(registration.id) }
                jsonPath("$.data[0].boss.socialType") { value(registration.boss.socialInfo.socialType.name) }
                jsonPath("$.data[0].boss.name") { value(registration.boss.name) }
                jsonPath("$.data[0].boss.businessNumber") { value(registration.boss.businessNumber.getNumberWithSeparator()) }
                jsonPath("$.data[0].store.name") { value(registration.store.name) }
                jsonPath("$.data[0].store.categories", hasSize<BossAccountRegistrationStoreResponse>(0))
                jsonPath("$.data[0].store.contactsNumber") { value(registration.store.contactsNumber.getNumberWithSeparator()) }
                jsonPath("$.data[0].store.certificationPhotoUrl") { value(registration.store.certificationPhotoUrl) }
            }
    }

}
