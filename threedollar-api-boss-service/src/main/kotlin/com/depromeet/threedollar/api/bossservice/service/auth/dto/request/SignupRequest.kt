package com.depromeet.threedollar.api.bossservice.service.auth.dto.request

import javax.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import com.depromeet.threedollar.api.bossservice.config.validator.BossName
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationStoreForm

data class SignupRequest(
    val token: String,
    val socialType: BossAccountSocialType,

    @field:BossName
    val bossName: String,

    val businessNumber: String,

    @field:Size(max = 20, message = "{store.name.size}")
    val storeName: String,

    @field:Size(max = 3, message = "{store.categoriesIds.size}")
    val storeCategoriesIds: Set<String>,

    val contactsNumber: String,

    @field:Size(max = 300, message = "{store.certificationPhotoUrl.size}")
    @field:URL(message = "{store.certificationPhotoUrl.url}")
    val certificationPhotoUrl: String,
) {

    fun toEntity(socialId: String): BossRegistration {
        return BossRegistration.of(
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
