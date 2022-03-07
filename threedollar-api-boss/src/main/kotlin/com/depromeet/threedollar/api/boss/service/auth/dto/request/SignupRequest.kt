package com.depromeet.threedollar.api.boss.service.auth.dto.request

import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.URL
import javax.validation.constraints.Size

data class SignupRequest(
    val token: String,
    val socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType,

    @Length(max = 30, message = "{account.name.length}")
    val bossName: String,

    val businessNumber: String,

    @field:Length(max = 30, message = "{store.name.length}")
    val storeName: String,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val storeCategoriesIds: Set<String>,

    val contactsNumber: String,

    @field:Length(max = 2048, message = "{store.certificationPhotoUrl.length}")
    @field:URL(message = "{store.certificationPhotoUrl.url}")
    val certificationPhotoUrl: String,
) {

    fun toEntity(socialId: String): Registration {
        return Registration(
            boss = RegistrationBossForm(
                socialInfo = com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo.of(socialId, socialType),
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
