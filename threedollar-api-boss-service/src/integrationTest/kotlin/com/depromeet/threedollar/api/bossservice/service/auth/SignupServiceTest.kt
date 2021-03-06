package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.IntegrationTest
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationStatus
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class SignupServiceTest(
    private val signupService: SignupService,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) : IntegrationTest() {

    @AfterEach
    fun cleanUp() {
        bossRegistrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
        bossStoreCategoryRepository.deleteAll()
    }

    @Nested
    inner class RegistrationBossAccountTest {

        @Test
        fun `신규 가입을 신청하면 Registration 데이터가 WAITING 상태로 추가된다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "000-00-00000"
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
            signupService.signUp(request, socialId)

            // then
            val registrations = bossRegistrationRepository.findAll()
            assertAll({
                assertThat(registrations).hasSize(1)
                assertThat(registrations[0].status).isEqualTo(BossRegistrationStatus.WAITING)
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

            bossRegistrationRepository.save(
                RegistrationFixture.create(
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
            assertThatThrownBy { signupService.signUp(request, socialId) }.isInstanceOf(ForbiddenException::class.java)
        }

        @Test
        fun `이미 가입한 계정인 경우 신규 가입 신청시 Conflict 예외가 발생합니다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "중식")

            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            bossAccountRepository.save(
                BossAccountFixture.create(
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
            assertThatThrownBy { signupService.signUp(request, socialId) }.isInstanceOf(ConflictException::class.java)
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
                signupService.signUp(request, socialId = "socialId")
            }.isInstanceOf(NotFoundException::class.java)
        }

    }


}

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String,
): Set<String> {
    return titles.asSequence()
        .map { title -> bossStoreCategoryRepository.save(BossStoreCategoryFixture.create(title)).id }
        .toSet()
}
