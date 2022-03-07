package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber

@TestFixture
object RegistrationCreator {

    fun create(
        socialId: String,
        socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType,
        bossName: String = "will",
        businessNumber: String = "123-12-12344",
        storeName: String = "가슴속 3천원",
        contactsNumber: String = "010-1234-1234",
        certificationPhotoUrl: String = "https://sample-photo.png",
        categoriesIds: Set<String> = setOf(),
        status: RegistrationStatus = RegistrationStatus.WAITING
    ): Registration {
        return Registration(
            boss = RegistrationBossForm(
                socialInfo = com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo(
                    socialId = socialId,
                    socialType = socialType
                ),
                name = bossName,
                businessNumber = BusinessNumber.of(businessNumber)
            ),
            store = RegistrationStoreForm(
                name = storeName,
                contactsNumber = ContactsNumber.of(contactsNumber),
                categoriesIds = categoriesIds,
                certificationPhotoUrl = certificationPhotoUrl
            ),
            status = status
        )
    }

}
