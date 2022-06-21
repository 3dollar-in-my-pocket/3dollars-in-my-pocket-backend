package com.depromeet.threedollar.api.adminservice.service.bossservice.registration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.adminservice.SetupAdminIntegrationTest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.BusinessNumber
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationStatus
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository

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
        fun `신규 가입신청을 승인하면 가입 신청 정보를 토대로 사장님 계정이 생성된다`() {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER
            val bossName = "가삼"
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
                    assertThat(it.isSetupNotification).isFalse()
                }
            })
        }

        @Test
        fun `신규 가입신청을 승인하면 세션 유지를 위해서 가입신청 ID가 사장님 계정의 ID가 된다`() {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER
            val bossName = "가삼"
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
                    assertThat(it.id).isEqualTo(registration.id)
                }
            })
        }

        @Test
        fun `신규 가입신청을 승인하면 해당 정보를 토대로 가게가 생성된다`() {
            // given
            val storeName = "행잉"
            val contactsNumber = "010-1234-1234"
            val categoriesIds = setOf("한식id", "중식id")
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
        fun `이미 승인된 가입 신청인 경우 다시 승인할 수 없다`() {
            // given
            val registration = RegistrationFixture.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.NAVER,
                status = BossRegistrationStatus.APPROVED
            )
            bossRegistrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가입 거부된 가입 신청인 경우 다시 승인할 수 없다`() {
            // given
            val registration = RegistrationFixture.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.NAVER,
                status = BossRegistrationStatus.REJECTED
            )
            bossRegistrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `해당하는 가입 정보가 없는 경우 NotFoundException`() {
            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration("NotFound Id") }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가입 승인시 이미 사장님 계정이 있는 경우 Conflict Exception`() {
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
        fun `사장님 가입 신청이 승인되면 해당 가입 신청은 APPROVE 상태로 변경된다`() {
            // given
            val registration = RegistrationFixture.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                status = BossRegistrationStatus.WAITING
            )
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
        fun `사장님 가입 신청이 거부되면 해당 가입 신청은 APPROVE 상태로 변경된다`() {
            // given
            val registration = RegistrationFixture.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                status = BossRegistrationStatus.WAITING
            )
            bossRegistrationRepository.save(registration)

            // when
            bossRegistrationAdminService.rejectBossRegistration(registration.id)

            // then
            val registrations = bossRegistrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(BossRegistrationStatus.REJECTED)
            })
        }

        @Test
        fun `사장님 가입 신청 거부시 해당하는 가입신청 정보가 없는경우 NotFoundException 에러가 발생한다`() {
            // when & then
            assertThatThrownBy { bossRegistrationAdminService.rejectBossRegistration(registrationId = "notFoundRegistrationId") }.isInstanceOf(NotFoundException::class.java)
        }

    }

}
