package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration

import com.depromeet.threedollar.domain.mongo.TestFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType

@TestFixture
object RegistrationCreator {

    fun create(
        socialId: String,
        socialType: BossAccountSocialType,
        bossName: String = "will",
        businessNumber: String = "000-00-00000",
        storeName: String = "가슴속 3천원",
        contactsNumber: String = "010-1234-1234",
        certificationPhotoUrl: String = "https://sample-photo.png",
        categoriesIds: Set<String> = setOf(),
        status: BossRegistrationStatus = BossRegistrationStatus.WAITING,
    ): BossRegistration {
        return BossRegistration(
            boss = RegistrationBossForm.of(
                socialId = socialId,
                socialType = socialType,
                name = bossName,
                businessNumber = businessNumber
            ),
            store = RegistrationStoreForm.of(
                name = storeName,
                contactsNumber = contactsNumber,
                categoriesIds = categoriesIds,
                certificationPhotoUrl = certificationPhotoUrl
            ),
            status = status
        )
    }

}
