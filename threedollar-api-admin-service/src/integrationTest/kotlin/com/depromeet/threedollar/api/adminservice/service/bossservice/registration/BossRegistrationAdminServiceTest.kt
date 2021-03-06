package com.depromeet.threedollar.api.adminservice.service.bossservice.registration

import com.depromeet.threedollar.api.adminservice.SetupAdminIntegrationTest
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request.RejectBossRegistrationRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationStatus
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class BossRegistrationAdminServiceTest(
    private val bossRegistrationAdminService: BossRegistrationAdminService,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
) : SetupAdminIntegrationTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossRegistrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

    @Nested
    inner class ApplyBossRegistrationTest {

        @Test
        fun `?????? ??????????????? ???????????? ?????? ?????? ????????? ????????? ????????? ????????? ????????????`() {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER
            val bossName = "??????"
            val businessNumber = "000-00-00000"

            val registration = RegistrationFixture.create(
                socialId = socialId,
                socialType = socialType,
                bossName = bossName,
                businessNumber = businessNumber,
                status = BossRegistrationStatus.WAITING
            )
            bossRegistrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertAll({
                assertThat(bossAccounts).hasSize(1)
                bossAccounts[0].let {
                    assertThat(it.socialInfo).isEqualTo(BossAccountSocialInfo.of(socialId, socialType))
                    assertThat(it.name).isEqualTo(bossName)
                    assertThat(it.businessNumber).isEqualTo(BusinessNumber.of(businessNumber))
                }
            })
        }

        @Test
        fun `?????? ??????????????? ???????????? ?????? ????????? ????????? ???????????? ID??? ????????? ????????? ID??? ??????`() {
            // given
            val registration = RegistrationFixture.create(status = BossRegistrationStatus.WAITING)
            bossRegistrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertAll({
                assertThat(bossAccounts).hasSize(1)
                bossAccounts[0].let {
                    assertThat(it.id).isEqualTo(registration.id)
                }
            })
        }

        @Test
        fun `?????? ??????????????? ???????????? ?????? ????????? ????????? ????????? ????????????`() {
            // given
            val storeName = "??????"
            val contactsNumber = "010-1234-1234"
            val categoriesIds = setOf("??????id", "??????id")
            val certificationImageUrl = "https://certification.png"

            val registration = RegistrationFixture.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                storeName = storeName,
                categoriesIds = categoriesIds,
                contactsNumber = contactsNumber,
                certificationPhotoUrl = certificationImageUrl,
            )
            bossRegistrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0].let {
                    assertThat(it.name).isEqualTo(storeName)
                    assertThat(it.imageUrl).isEqualTo(certificationImageUrl)
                    assertThat(it.introduction).isNull()
                    assertThat(it.contactsNumber).isEqualTo(ContactsNumber.of(contactsNumber))
                    assertThat(it.snsUrl).isNull()
                    assertThat(it.menus).isEmpty()
                    assertThat(it.appearanceDays).isEmpty()
                    assertThat(it.categoriesIds).isEqualTo(categoriesIds)
                }
            })
        }

        @Test
        fun `?????? ????????? ?????? ????????? ?????? ?????? ????????? ??? ??????`() {
            // given
            val registration = RegistrationFixture.create(status = BossRegistrationStatus.APPROVED)
            bossRegistrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `?????? ????????? ?????? ????????? ?????? ?????? ????????? ??? ??????`() {
            // given
            val registration = RegistrationFixture.create(status = BossRegistrationStatus.REJECTED)
            bossRegistrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `???????????? ?????? ????????? ?????? ?????? NotFoundException`() {
            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration("NotFound Id") }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `?????? ????????? ?????? ????????? ????????? ?????? ?????? Conflict Exception`() {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER

            val bossAccount = BossAccountFixture.create(
                socialId = socialId,
                socialType = socialType
            )
            bossAccountRepository.save(bossAccount)

            val registration = RegistrationFixture.create(
                socialId = socialId,
                socialType = socialType,
                status = BossRegistrationStatus.WAITING
            )
            bossRegistrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(ConflictException::class.java)
        }

        @Test
        fun `????????? ?????? ????????? ???????????? ?????? ?????? ????????? APPROVE ????????? ????????????`() {
            // given
            val registration = RegistrationFixture.create(status = BossRegistrationStatus.WAITING)
            bossRegistrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val registrations = bossRegistrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(BossRegistrationStatus.APPROVED)
            })
        }

    }

    @Nested
    inner class RejectBossRegistrationTest {

        @Test
        fun `????????? ?????? ????????? ???????????? ?????? ?????? ????????? APPROVE ????????? ????????????`() {
            // given
            val registration = RegistrationFixture.create(status = BossRegistrationStatus.WAITING)
            bossRegistrationRepository.save(registration)

            val request = RejectBossRegistrationRequest(
                rejectReason = BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER,
            )

            // when
            bossRegistrationAdminService.rejectBossRegistration(registrationId = registration.id, request = request)

            // then
            val registrations = bossRegistrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(BossRegistrationStatus.REJECTED)
                assertThat(registrations[0].rejectReasonType).isEqualTo(BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER)
            })
        }

        @Test
        fun `????????? ?????? ?????? ????????? ???????????? ???????????? ????????? ???????????? NotFoundException ????????? ????????????`() {
            // given
            val request = RejectBossRegistrationRequest(
                rejectReason = BossRegistrationRejectReasonType.INVALID_BUSINESS_NUMBER,
            )

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.rejectBossRegistration(registrationId = "notFoundRegistrationId", request = request) }.isInstanceOf(NotFoundException::class.java)
        }

    }

}
