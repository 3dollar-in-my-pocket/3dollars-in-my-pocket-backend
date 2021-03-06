package com.depromeet.threedollar.api.adminservice.controller.bossservice.registration

import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.listener.bossservice.push.BossSendPushListener
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request.RejectBossRegistrationRequest
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.response.BossAccountRegistrationStoreResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType.GOOGLE
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

internal class BossRegistrationAdminControllerTest(
    private val bossRegistrationRepository: BossRegistrationRepository,
) : SetupAdminControllerTest() {

    @MockkBean
    private lateinit var bossSendPushListener: BossSendPushListener

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossRegistrationRepository.deleteAll()
    }

    @DisplayName("PUT /v1/boss/account/registration/{registration.id}/reject")
    @Test
    fun `사장님 계정 가입 신청을 승인하고 푸시 알림을 전송합니다`() {
        // given
        every { bossSendPushListener.sendBossRegistrationApproveMessage(any()) } returns Unit

        val registration = RegistrationFixture.create()
        bossRegistrationRepository.save(registration)

        // when
        mockMvc.put("/v1/boss/account/registration/${registration.id}/apply") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }

        // then
        verify(exactly = 1) { bossSendPushListener.sendBossRegistrationApproveMessage(any()) }
    }

    @DisplayName("PUT /v1/boss/account/registration/{registration.id}/reject")
    @Test
    fun `사장님 계정 가입 신청을 반려하고 푸시 알림을 전송합니다`() {
        // given
        every { bossSendPushListener.sendBossRegistrationDenyMessage(any()) } returns Unit

        val registration = RegistrationFixture.create()
        bossRegistrationRepository.save(registration)

        val request = RejectBossRegistrationRequest(
            rejectReason = BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER,
        )

        // when
        mockMvc.put("/v1/boss/account/registration/${registration.id}/reject") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }

        // then
        verify(exactly = 1) { bossSendPushListener.sendBossRegistrationDenyMessage(any()) }
    }

    @DisplayName("GET /v1/boss/account/registrations")
    @Test
    fun `사장님 계정의 가입 신청 목록을 조회합니다`() {
        // given
        val registration = RegistrationFixture.create("social-id", GOOGLE)
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
