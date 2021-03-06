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
        fun `?????? ????????? ???????????? Registration ???????????? WAITING ????????? ????????????`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "??????", "??????")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "000-00-00000"
            val storeName = "????????? 3?????? ????????? ??????"
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
        fun `?????? ?????? ????????? ?????? ?????? ???????????? ?????? Forbidden ????????? ????????????`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "??????")
            val bossName = "will"
            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            val businessNumber = "210-10-12345"
            val storeName = "????????? 3?????? ????????? ??????"
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
        fun `?????? ????????? ????????? ?????? ?????? ?????? ????????? Conflict ????????? ???????????????`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "??????", "??????")

            val socialId = "social-id"
            val socialType = BossAccountSocialType.APPLE
            bossAccountRepository.save(
                BossAccountFixture.create(
                    socialId = socialId,
                    socialType = socialType
                )
            )

            val request = SignupRequest(
                bossName = "????????? ??????",
                token = "Dummy Access Token",
                socialType = socialType,
                businessNumber = "123-82-1134",
                storeName = "????????? ?????? ??????",
                storeCategoriesIds = categoriesIds,
                contactsNumber = "010-1234-1234",
                certificationPhotoUrl = "https://url.com"
            )

            // when & then
            assertThatThrownBy { signupService.signUp(request, socialId) }.isInstanceOf(ConflictException::class.java)
        }

        @Test
        fun `?????? ?????? ????????? ???????????? ?????? ??????????????? ???????????? ?????? ?????? NotFound ????????? ????????????`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "??????")

            val request = SignupRequest(
                bossName = "will",
                token = "Dummy Access Token",
                socialType = BossAccountSocialType.APPLE,
                businessNumber = "210-10-1234",
                storeName = "????????? 3??????",
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
