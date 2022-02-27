package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.*
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryCreator
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.registration.*
import com.depromeet.threedollar.document.boss.document.withdrawal.BossWithdrawalAccountRepository
import com.depromeet.threedollar.document.common.document.BusinessNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class SignupServiceTest(
    private val authCommonService: AuthCommonService,
    private val registrationRepository: RegistrationRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @AfterEach
    fun cleanUp() {
        registrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
        bossStoreCategoryRepository.deleteAll()
        bossWithdrawalAccountRepository.deleteAll()
    }

    @Nested
    inner class SignUp {

        @Test
        fun `신규 가입을 신청하면 Registration 데이터가 WAITING 상태로 추가된다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "210-10-12345"
            val storeName = "가슴속 3천원 붕어빵 가게"
            val contactsNumber = "010-1234-1234"
            val certificationPhotoUrl = "https://example-photo.png"

            val request = SignupRequest(
                bossName = bossName,
                token = "Dummy Access Token",
                socialType = socialType,
                businessNumber = businessNumber,
                storeName = storeName,
                storeCategoriesIds = categoriesIds,
                contactsNumber = contactsNumber,
                certificationPhotoUrl = certificationPhotoUrl
            )

            // when
            authCommonService.signUp(request, socialId)

            // then
            val registrations = registrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(RegistrationStatus.WAITING)
                registrations[0].boss.let {
                    assertThat(it.name).isEqualTo(bossName)
                    assertThat(it.socialInfo.socialId).isEqualTo(socialId)
                    assertThat(it.socialInfo.socialType).isEqualTo(socialType)
                    assertThat(it.businessNumber.getNumberWithSeparator()).isEqualTo(businessNumber)
                }
                registrations[0].store.let {
                    assertThat(it.name).isEqualTo(storeName)
                    assertThat(it.categoriesIds).containsExactlyInAnyOrderElementsOf(categoriesIds)
                    assertThat(it.contactsNumber.getNumberWithSeparator()).isEqualTo(contactsNumber)
                    assertThat(it.certificationPhotoUrl).isEqualTo(certificationPhotoUrl)
                }
            })
        }

        @Test
        fun `신규 가입 신청시 이미 가입 신청중인 경우 Forbidden 예외가 발생한다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "210-10-12345"
            val storeName = "가슴속 3천원 붕어빵 가게"
            val contactsNumber = "010-1234-1234"
            val certificationPhotoUrl = "https://example-photo.png"

            registrationRepository.save(
                RegistrationCreator.create(
                    socialId = socialId,
                    socialType = socialType
                )
            )

            val request = SignupRequest(
                bossName = bossName,
                token = "Dummy Access Token",
                socialType = socialType,
                businessNumber = businessNumber,
                storeName = storeName,
                storeCategoriesIds = categoriesIds,
                contactsNumber = contactsNumber,
                certificationPhotoUrl = certificationPhotoUrl
            )

            // when & then
            assertThatThrownBy { authCommonService.signUp(request, socialId) }.isInstanceOf(ForbiddenException::class.java)
        }

        @Test
        fun `이미 가입한 계정인 경우 신규 가입 신청시 Conflict 예외가 발생합니다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "중식")

            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            bossAccountRepository.save(
                BossAccountCreator.create(
                    socialId = socialId,
                    socialType = socialType
                )
            )

            val request = SignupRequest(
                bossName = "사장님 이름",
                token = "Dummy Access Token",
                socialType = socialType,
                businessNumber = "123-82-1134",
                storeName = "사장님 가게 이름",
                storeCategoriesIds = categoriesIds,
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://url.com"
            )

            // when & then
            assertThatThrownBy { authCommonService.signUp(request, socialId) }.isInstanceOf(ConflictException::class.java)
        }

        @Test
        fun `신규 가입 신청시 존재하지 않는 카테고리가 하나라도 있는 경우 NotFound 에러가 발생한다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식")

            val request = SignupRequest(
                bossName = "will",
                token = "Dummy Access Token",
                socialType = BossAccountSocialType.APPLE,
                businessNumber = "210-10-1234",
                storeName = "가슴속 3천원",
                storeCategoriesIds = categoriesIds + "NotExists Store Category Id",
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://example-photo.png"
            )

            // when & then
            assertThatThrownBy {
                authCommonService.signUp(request, socialId = "socialId")
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class SignOut {

        @Test
        fun `회원탈퇴시 BossAccount 계정 정보가 삭제된다`() {
            // given
            val bossAccount = BossAccountCreator.create(
                socialId = "socialId",
                socialType = BossAccountSocialType.APPLE
            )
            bossAccountRepository.save(bossAccount)

            // when
            authCommonService.signOut(bossAccount.id)

            // then
            val bossAccounts = bossAccountRepository.findAll()
            assertThat(bossAccounts).isEmpty()
        }

        @Test
        fun `회원탈퇴시 계정정보가 BossWithdrawalAccount에 백업된다`() {
            // given
            val socialId = "auth-social-id"
            val socialType = BossAccountSocialType.APPLE
            val name = "강승호"
            val pushSettingsStatus = PushSettingsStatus.OFF
            val businessNumber = BusinessNumber.of("123-12-12345")

            val bossAccount = BossAccountCreator.create(
                socialId = socialId,
                socialType = socialType,
                name = name,
                businessNumber = businessNumber,
                pushSettingsStatus = pushSettingsStatus
            )
            bossAccountRepository.save(bossAccount)

            // when
            authCommonService.signOut(bossAccount.id)

            // then
            val withdrawalAccounts = bossWithdrawalAccountRepository.findAll()
            assertAll({
                assertThat(withdrawalAccounts).hasSize(1)
                withdrawalAccounts[0].let {
                    assertThat(it.name).isEqualTo(name)
                    assertThat(it.socialInfo).isEqualTo(BossAccountSocialInfo.of(socialId, socialType))
                    assertThat(it.pushSettingsStatus).isEqualTo(pushSettingsStatus)
                    assertThat(it.businessNumber).isEqualTo(businessNumber)

                    assertThat(it.backupInfo.bossId).isEqualTo(bossAccount.id)
                    assertThat(it.backupInfo.bossCreatedDateTime).isEqualToIgnoringNanos(bossAccount.createdDateTime)
                }
            })
        }

    }

}

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String
): Set<String> {
    return titles.map {
        bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id
    }.toSet()
}
