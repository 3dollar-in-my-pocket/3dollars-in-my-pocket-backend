package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

internal class RegistrationTest : FunSpec({

    context("가입 신청") {
        test("가입 신청 정보를 바탕으로 BossAccount를 생성한다") {
            // given
            val socialId = "social-id"
            val socialType = BossAccountSocialType.NAVER
            val bossName = "가삼"
            val businessNumber = "010-12-12345"

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
            bossAccount.also {
                it.name shouldBe bossName
                it.socialInfo shouldBe BossAccountSocialInfo.of(socialId, socialType)
                it.businessNumber shouldBe BusinessNumber.of(businessNumber)
            }
        }

        test("가입 신청 정보를 바탕으로 BossStore를 생성한다") {
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
            bossStore.also {
                it.name shouldBe storeName
                it.contactsNumber shouldBe ContactsNumber.of(contactsNumber)
                it.categoriesIds shouldBe categoriesIds
            }
        }
    }

})
