package com.depromeet.threedollar.boss.api.service.registration

import com.depromeet.threedollar.boss.api.service.registration.dto.request.ApplyRegistrationRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.*
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategory
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.registration.Registration
import com.depromeet.threedollar.document.boss.document.registration.RegistrationBossForm
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import com.depromeet.threedollar.document.boss.document.registration.RegistrationStoreForm
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.BusinessNumber
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class RegistrationServiceTest(
    private val registrationService: RegistrationService,
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

    @Test
    fun `신규 가입을 신청하면 Registration 데이터가 추가된다`() {
        // given
        val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
        val bossName = "will"
        val socialId = "social-id"
        val socialType = BossAccountSocialType.APPLE
        val businessNumber = "210-10-12345"
        val storeName = "가슴속 3천원 붕어빵 가게"
        val contactsNumber = "010-1234-1234"
        val certificationPhotoUrl = "https://example-photo.png"

        val request = ApplyRegistrationRequest(
            bossName = bossName,
            socialId = socialId,
            socialType = socialType,
            businessNumber = businessNumber,
            storeName = storeName,
            storeCategoriesIds = categoriesIds,
            contactsNumber = contactsNumber,
            certificationPhotoUrl = certificationPhotoUrl
        )

        // when
        registrationService.applyRegistration(request)

        // then
        val registrations = registrationRepository.findAll()
        assertThat(registrations).hasSize(1)
        registrations[0].boss.let {
            assertThat(it.name).isEqualTo(bossName)
            assertThat(it.socialInfo.socialId).isEqualTo(socialId)
            assertThat(it.socialInfo.socialType).isEqualTo(socialType)
            assertThat(it.businessNumber.getNumber()).isEqualTo(businessNumber)
        }
        registrations[0].store.let {
            assertThat(it.name).isEqualTo(storeName)
            assertThat(it.categoriesIds).containsExactlyInAnyOrderElementsOf(categoriesIds)
            assertThat(it.contactsNumber.getNumber()).isEqualTo(contactsNumber)
            assertThat(it.certificationPhotoUrl).isEqualTo(certificationPhotoUrl)
        }
    }

    @Test
    fun `신규 가입 신청시 이미 가입 신청중인 경우 Conflict 예외가 발생한다`() {
        // given
        val categoriesIds = createCategory(bossStoreCategoryRepository, "한식")
        val bossName = "will"
        val socialId = "social-id"
        val socialType = BossAccountSocialType.APPLE
        val businessNumber = "210-10-12345"
        val storeName = "가슴속 3천원 붕어빵 가게"
        val contactsNumber = "010-1234-1234"
        val certificationPhotoUrl = "https://example-photo.png"

        registrationRepository.save(createRegistration(socialId, socialType))

        val request = ApplyRegistrationRequest(
            bossName = bossName,
            socialId = socialId,
            socialType = socialType,
            businessNumber = businessNumber,
            storeName = storeName,
            storeCategoriesIds = categoriesIds,
            contactsNumber = contactsNumber,
            certificationPhotoUrl = certificationPhotoUrl
        )

        // when & then
        assertThatThrownBy { registrationService.applyRegistration(request) }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `이미 가입한 계정인 경우 신규 가입 신청시 Conflict 예외가 발생합니다`() {
        // given
        val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "중식")

        val socialId = "social-id"
        val socialType = BossAccountSocialType.APPLE
        bossAccountRepository.save(createBossAccount(socialId, socialType))

        val request = ApplyRegistrationRequest(
            bossName = "사장님 이름",
            socialId = socialId,
            socialType = socialType,
            businessNumber = "123-82-1134",
            storeName = "사장님 가게 이름",
            storeCategoriesIds = categoriesIds,
            contactsNumber = "010-1234-1234",
            certificationPhotoUrl = "https://url.com"
        )

        // when & then
        assertThatThrownBy { registrationService.applyRegistration(request) }.isInstanceOf(ConflictException::class.java)
    }

    @Test
    fun `신규 가입 신청시 존재하지 않는 카테고리가 하나라도 있는 경우 NotFound 에러가 발생한다`() {
        // given
        val categoriesIds = createCategory(bossStoreCategoryRepository, "한식")

        val request = ApplyRegistrationRequest(
            bossName = "will",
            socialId = "social-id",
            socialType = BossAccountSocialType.APPLE,
            businessNumber = "210-10-1234",
            storeName = "가슴속 3천원",
            storeCategoriesIds = categoriesIds + "NotExists Store Category Id",
            contactsNumber = "010-1234-1234",
            certificationPhotoUrl = "https://example-photo.png"
        )

        // when & then
        assertThatThrownBy { registrationService.applyRegistration(request) }.isInstanceOf(NotFoundException::class.java)
    }

}

private fun createBossAccount(socialId: String, socialType: BossAccountSocialType): BossAccount {
    return BossAccount(
        name = "사장님 성함",
        socialInfo = BossAccountSocialInfo(socialId, socialType)
    )
}

private fun createRegistration(socialId: String, socialType: BossAccountSocialType): Registration {
    return Registration(
        boss = RegistrationBossForm(
            socialInfo = BossAccountSocialInfo(socialId, socialType),
            name = "name",
            businessNumber = BusinessNumber.of("123-01-1234")
        ),
        store = RegistrationStoreForm(
            name = "가게 이름",
            categoriesIds = mutableListOf("1", "3"),
            contactsNumber = ContactsNumber.of("010-1234-1234"),
            certificationPhotoUrl = "https://sample.png"
        )
    )
}

private fun createCategory(bossStoreCategoryRepository: BossStoreCategoryRepository, vararg titles: String): List<String> {
    return titles.map {
        bossStoreCategoryRepository.save(BossStoreCategory(it, 10)).id
    }
}