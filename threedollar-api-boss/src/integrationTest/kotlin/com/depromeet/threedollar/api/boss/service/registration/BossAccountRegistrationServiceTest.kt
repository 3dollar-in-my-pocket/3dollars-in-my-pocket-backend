package com.depromeet.threedollar.api.boss.service.registration

import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStatus
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BossAccountRegistrationServiceOneTest(
    private val bossAccountRegistrationService: BossAccountRegistrationService,
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) : FunSpec({

    afterEach {
        withContext(Dispatchers.IO) {
            registrationRepository.deleteAll()
            bossAccountRepository.deleteAll()
            bossStoreCategoryRepository.deleteAll()
        }
    }

    context("신규 회원가입 요청") {
        test("신규 가입을 신청하면 Registration 테이블에 WAITING 상태인 데이터가 추가된다") {
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
            withContext(Dispatchers.IO) {
                bossAccountRegistrationService.signUp(request, socialId)
            }

            // then
            val registrations = registrationRepository.findAll()
            registrations shouldHaveSize 1
            registrations[0].also {
                it.status shouldBe RegistrationStatus.WAITING
            }
            registrations[0].boss.also {
                it.name shouldBe bossName
                it.socialInfo shouldBe BossAccountSocialInfo.of(socialId, socialType)
                it.businessNumber.getNumberWithSeparator() shouldBe businessNumber
            }
            registrations[0].store.also {
                it.name shouldBe storeName
                it.categoriesIds shouldContainExactlyInAnyOrder categoriesIds
                it.contactsNumber.getNumberWithSeparator() shouldBe contactsNumber
                it.certificationPhotoUrl shouldBe certificationPhotoUrl
            }
        }

        test("신규 가입 신청시, 이미 WAITING 상태인 가입 신청이 있는 경우 Forbidden 에러가 발생한다") {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "210-10-12345"
            val storeName = "가슴속 3천원 붕어빵 가게"
            val contactsNumber = "010-1234-1234"
            val certificationPhotoUrl = "https://example-photo.png"

            withContext(Dispatchers.IO) {
                registrationRepository.save(
                    RegistrationCreator.create(
                        socialId = socialId,
                        socialType = socialType
                    )
                )
            }

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
            shouldThrowExactly<ForbiddenException> {
                bossAccountRegistrationService.signUp(request, socialId)
            }
        }

        test("신규 가입 신청시, 이미 가입 완료한 계정이 있는 경우 Conflict 에러가 발생한다") {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "중식")

            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            withContext(Dispatchers.IO) {
                bossAccountRepository.save(
                    BossAccountCreator.create(
                        socialId = socialId,
                        socialType = socialType
                    )
                )
            }

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
            shouldThrowExactly<ConflictException> {
                bossAccountRegistrationService.signUp(request, socialId)
            }
        }

        test("신규 가입 신청시, 하나라도 존재하지 않는 카테고리가 있는 경우 NotFound 에러가 발생한다") {
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
            shouldThrowExactly<NotFoundException> {
                bossAccountRegistrationService.signUp(request, socialId = "socialId")
            }
        }
    }

})

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String
): Set<String> {
    return titles.asSequence()
        .map { bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id }
        .toSet()
}
