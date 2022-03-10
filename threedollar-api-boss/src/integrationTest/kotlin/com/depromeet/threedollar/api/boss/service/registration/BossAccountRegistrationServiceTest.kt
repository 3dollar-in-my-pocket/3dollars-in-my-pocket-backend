package com.depromeet.threedollar.api.boss.service.registration

import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStatus
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossAccountRegistrationServiceTest(
    private val bossAccountRegistrationService: BossAccountRegistrationService,
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @AfterEach
    fun cleanUp() {
        registrationRepository.deleteAll()
        bossAccountRepository.deleteAll()
        bossStoreCategoryRepository.deleteAll()
    }

    @Nested
    inner class 계정_신규_가입_신청 {

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
            bossAccountRegistrationService.applyForBossAccountRegistration(request, socialId)

            // then
            val registrations = registrationRepository.findAll()
            assertAll({
                Assertions.assertThat(registrations).hasSize(1)
                Assertions.assertThat(registrations[0].status).isEqualTo(RegistrationStatus.WAITING)
                registrations[0].boss.let {
                    Assertions.assertThat(it.name).isEqualTo(bossName)
                    Assertions.assertThat(it.socialInfo.socialId).isEqualTo(socialId)
                    Assertions.assertThat(it.socialInfo.socialType).isEqualTo(socialType)
                    Assertions.assertThat(it.businessNumber.getNumberWithSeparator()).isEqualTo(businessNumber)
                }
                registrations[0].store.let {
                    Assertions.assertThat(it.name).isEqualTo(storeName)
                    Assertions.assertThat(it.categoriesIds).containsExactlyInAnyOrderElementsOf(categoriesIds)
                    Assertions.assertThat(it.contactsNumber.getNumberWithSeparator()).isEqualTo(contactsNumber)
                    Assertions.assertThat(it.certificationPhotoUrl).isEqualTo(certificationPhotoUrl)
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
            Assertions.assertThatThrownBy { bossAccountRegistrationService.applyForBossAccountRegistration(request, socialId) }.isInstanceOf(ForbiddenException::class.java)
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
            Assertions.assertThatThrownBy { bossAccountRegistrationService.applyForBossAccountRegistration(request, socialId) }.isInstanceOf(ConflictException::class.java)
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
            Assertions.assertThatThrownBy {
                bossAccountRegistrationService.applyForBossAccountRegistration(request, socialId = "socialId")
            }.isInstanceOf(NotFoundException::class.java)
        }

    }


}

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String
): Set<String> {
    return titles.asSequence()
        .map { bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id }
        .toSet()
}
