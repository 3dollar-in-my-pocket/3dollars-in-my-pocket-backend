package com.depromeet.threedollar.document.boss.document.registration

import com.depromeet.threedollar.document.TestFixture
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber

@TestFixture
object RegistrationCreator {

    fun create(
        socialId: String,
        socialType: BossAccountSocialType,
        bossName: String = "will",
        businessNumber: String = "123-12-1234",
        storeName: String = "가슴속 3천원",
        contactsNumber: String = "010-1234-1234",
        certificationPhotoUrl: String = "https://sample-photo.png",
        status: RegistrationStatus = RegistrationStatus.WAITING
    ): Registration {
        return Registration(
            boss = RegistrationBossForm(
                socialInfo = BossAccountSocialInfo(
                    socialId = socialId,
                    socialType = socialType
                ),
                name = bossName,
                businessNumber = BusinessNumber.of(businessNumber)
            ),
            store = RegistrationStoreForm(
                name = storeName,
                contactsNumber = ContactsNumber.of(contactsNumber),
                certificationPhotoUrl = certificationPhotoUrl
            ),
            status = status
        )
    }

}
