package com.depromeet.threedollar.domain.mongo.boss.domain.registration

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
        val socialType = com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType.NAVER
        val bossName = "가삼"
        val businessNumber = "010-12-12345"

        val registration = RegistrationCreator.create(
            socialId = socialId,
            socialType = socialType,
            bossName = bossName,
            businessNumber = businessNumber
        )

        // when
        val bossAccount = registration.toBossAccount()

        // then
        assertAll({
            bossAccount.let {
                assertThat(it.name).isEqualTo(bossName)
                assertThat(it.socialInfo).isEqualTo(com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo.of(socialId, socialType))
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
            socialType = com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType.NAVER,
            storeName = storeName,
            categoriesIds = categoriesIds,
            contactsNumber = contactsNumber,
        )

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
