package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

internal class RegistrationTest {

    @Test
    fun `가입신청을 바탕으로 BossAccout를 생성한다`() {
        // given
        val socialId = "social-id"
        val socialType = BossAccountSocialType.NAVER
        val bossName = "가삼"
        val businessNumber = "000-00-00000"

        val registration = RegistrationCreator.create(
            socialId = socialId,
            socialType = socialType,
            bossName = bossName,
            businessNumber = businessNumber
        )
        registration.id = "registrationId"

        // when
        val bossAccount = registration.toBossAccount()

        // then
        assertAll({
            bossAccount.let {
                assertThat(it.name).isEqualTo(bossName)
                assertThat(it.socialInfo).isEqualTo(BossAccountSocialInfo.of(socialId, socialType))
                assertThat(it.businessNumber).isEqualTo(BusinessNumber.of(businessNumber))
            }
        })
    }

    @Test
    fun `가입신청을 바탕으로 BossStore를 생성한다`() {
        // given
        val bossId = "bossId"
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
        registration.id = "registrationId"

        // when
        val bossStore = registration.toBossStore(bossId)

        // then
        assertAll({
            bossStore.let {
                assertThat(it.name).isEqualTo(storeName)
                assertThat(it.contactsNumber).isEqualTo(ContactsNumber.of(contactsNumber))
                assertThat(it.categoriesIds).isEqualTo(categoriesIds)
            }
        })
    }

}
