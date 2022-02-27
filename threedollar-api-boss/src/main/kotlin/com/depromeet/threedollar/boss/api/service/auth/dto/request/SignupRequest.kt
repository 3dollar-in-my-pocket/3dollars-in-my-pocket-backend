package com.depromeet.threedollar.boss.api.service.auth.dto.request

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.boss.document.registration.Registration
import com.depromeet.threedollar.document.boss.document.registration.RegistrationBossForm
import com.depromeet.threedollar.document.boss.document.registration.RegistrationStoreForm
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.Size

data class SignupRequest(
    val token: String,
    val socialType: BossAccountSocialType,

    @Length(max = 30, message = "{account.name.length}")
    val bossName: String,
    val businessNumber: String,

    @field:Length(max = 30, message = "{store.name.length}")
    val storeName: String,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val storeCategoriesIds: Set<String>,

    val contactsNumber: String,

    @field:URL(message = "{store.certificationPhotoUrl.url}")
    val certificationPhotoUrl: String,
) {

    fun toEntity(socialId: String): Registration {
        return Registration(
            boss = RegistrationBossForm(
                socialInfo = BossAccountSocialInfo.of(socialId, socialType),
                name = bossName,
                businessNumber = BusinessNumber.of(businessNumber)
            ),
            store = RegistrationStoreForm(
                name = storeName,
                categoriesIds = storeCategoriesIds,
                contactsNumber = ContactsNumber.of(contactsNumber),
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
    }

}
