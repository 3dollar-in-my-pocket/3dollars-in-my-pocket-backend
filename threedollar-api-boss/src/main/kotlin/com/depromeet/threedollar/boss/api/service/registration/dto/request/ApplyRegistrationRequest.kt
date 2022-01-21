package com.depromeet.threedollar.boss.api.service.registration.dto.request

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.boss.document.registration.Registration
import com.depromeet.threedollar.document.boss.document.registration.RegistrationBossForm
import com.depromeet.threedollar.document.boss.document.registration.RegistrationStoreForm

data class ApplyRegistrationRequest(
    val bossName: String,
    val socialId: String,
    val socialType: BossAccountSocialType,
    val businessNumber: String,
    val storeName: String,
    val storeCategoriesIds: List<String>,
    val contactsNumber: String,
    val certificationPhotoUrl: String
) {

    fun toEntity(): Registration {
        return Registration(
            boss = RegistrationBossForm(
                socialInfo = BossAccountSocialInfo(socialId, socialType),
                name = bossName,
                businessNumber = BusinessNumber.of(businessNumber)
            ),
            store = RegistrationStoreForm(
                name = storeName,
                contactsNumber = ContactsNumber.of(contactsNumber),
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
    }

}
