package com.depromeet.threedollar.api.boss.service.auth.dto.request

import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.api.boss.config.validator.BossName
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm

data class SignupRequest(
    val token: String,
    val socialType: BossAccountSocialType,

    @field:BossName
    val bossName: String,

    val businessNumber: String,

    @field:Size(max = 30, message = "{store.name.size}")
    val storeName: String,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val storeCategoriesIds: Set<String>,

    val contactsNumber: String,

    @field:Size(max = 2048, message = "{store.certificationPhotoUrl.size}")
    @field:URL(message = "{store.certificationPhotoUrl.url}")
    val certificationPhotoUrl: String,
) {

    fun toEntity(socialId: String): Registration {
        return Registration.of(
            boss = RegistrationBossForm.of(
                socialId = socialId,
                socialType = socialType,
                name = bossName,
                businessNumber = businessNumber
            ),
            store = RegistrationStoreForm.of(
                name = storeName,
                categoriesIds = storeCategoriesIds,
                contactsNumber = contactsNumber,
                certificationPhotoUrl = certificationPhotoUrl
            )
        )
    }

}
