package com.depromeet.threedollar.api.admin.service.boss.registration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.admin.service.SetupAdminServiceTest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStatus
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber

internal class BossAccountRegistrationAdminServiceTest(
    private val bossRegistrationAdminService: BossAccountRegistrationAdminService,
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository
) : SetupAdminServiceTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        registrationRepository.deleteAll()
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

            val registration = RegistrationCreator.create(
                socialId = socialId,
                socialType = socialType,
                bossName = bossName,
                businessNumber = businessNumber,
                status = RegistrationStatus.WAITING
            )
            registrationRepository.save(registration)

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

            val registration = RegistrationCreator.create(
                socialId = socialId,
                socialType = socialType,
                bossName = bossName,
                businessNumber = businessNumber,
                status = RegistrationStatus.WAITING
            )
            registrationRepository.save(registration)

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

            val registration = RegistrationCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                storeName = storeName,
                categoriesIds = categoriesIds,
                contactsNumber = contactsNumber,
            )
            registrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0].let {
                    assertThat(it.name).isEqualTo(storeName)
                    assertThat(it.imageUrl).isNull()
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
            val registration = RegistrationCreator.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.NAVER,
                status = RegistrationStatus.APPROVED
            )
            registrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가입 거부된 가입 신청인 경우 다시 승인할 수 없다`() {
            // given
            val registration = RegistrationCreator.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.NAVER,
                status = RegistrationStatus.REJECTED
            )
            registrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `해당하는 가입 정보가 없는 경우 NotFoundException`() {
            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration("Not Found Registration Id") }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가입 승인시 이미 사장님 계정이 있는 경우 Conflict Exception`() {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER

            val bossAccount = BossAccountCreator.create(
                socialId = socialId,
                socialType = socialType
            )
            bossAccountRepository.save(bossAccount)

            val registration = RegistrationCreator.create(
                socialId = socialId,
                socialType = socialType,
                status = RegistrationStatus.WAITING
            )
            registrationRepository.save(registration)

            // when & then
            assertThatThrownBy { bossRegistrationAdminService.applyBossRegistration(registration.id) }.isInstanceOf(ConflictException::class.java)
        }

        @Test
        fun `사장님 가입 신청이 승인되면 해당 가입 신청은 APPROVE 상태로 변경된다`() {
            // given
            val registration = RegistrationCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                status = RegistrationStatus.WAITING
            )
            registrationRepository.save(registration)

            // when
            bossRegistrationAdminService.applyBossRegistration(registration.id)

            // then
            val registrations = registrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(RegistrationStatus.APPROVED)
            })
        }

    }

    @Nested
    inner class RejectBossRegistrationTest {

        @Test
        fun `사장님 가입 신청이 거부되면 해당 가입 신청은 APPROVE 상태로 변경된다`() {
            // given
            val registration = RegistrationCreator.create(
                socialId = "social-id",
                socialType = BossAccountSocialType.NAVER,
                status = RegistrationStatus.WAITING
            )
            registrationRepository.save(registration)

            // when
            bossRegistrationAdminService.rejectBossRegistration(registration.id)

            // then
            val registrations = registrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(RegistrationStatus.REJECTED)
            })
        }

        @Test
        fun `사장님 가입 신청 거부시 해당하는 가입신청 정보가 없는경우 NotFoundException 에러가 발생한다`() {
            // when & then
            assertThatThrownBy { bossRegistrationAdminService.rejectBossRegistration(registrationId = "notFoundRegistrationId") }.isInstanceOf(NotFoundException::class.java)
        }

    }

}
