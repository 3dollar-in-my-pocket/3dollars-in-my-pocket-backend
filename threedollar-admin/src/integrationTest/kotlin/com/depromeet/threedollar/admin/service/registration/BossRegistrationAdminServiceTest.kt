package com.depromeet.threedollar.admin.service.registration

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.*
import com.depromeet.threedollar.document.boss.document.registration.RegistrationCreator
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import com.depromeet.threedollar.document.boss.document.registration.RegistrationStatus
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreStatus
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class BossRegistrationAdminServiceTest(
    private val bossRegistrationAdminService: BossRegistrationAdminService,
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository
) {

    @AfterEach
    fun cleanUp() {
        registrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

    @Test
    fun `신규 가입신청을 승인하면 해당 정보를 토대로 사장님 계정이 생성된다`() {
        // given
        val socialId = "social-id"
        val socialType = BossAccountSocialType.NAVER
        val bossName = "가삼"
        val businessNumber = "010-12-12345"

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
                assertThat(it.socialInfo).isEqualTo(BossAccountSocialInfo(socialId, socialType))
                assertThat(it.name).isEqualTo(bossName)
                assertThat(it.businessNumber).isEqualTo(BusinessNumber.of(businessNumber))
                assertThat(it.pushSettingsStatus).isEqualTo(PushSettingsStatus.OFF)
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
                assertThat(it.status).isEqualTo(BossStoreStatus.ACTIVE)
            }
        })
    }

    @Test
    fun `이미 승인된 가입 신청인 경우 다시 승인할 수 없다`() {
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

}