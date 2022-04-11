package com.depromeet.threedollar.api.admin.service.boss.registration.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm

data class BossAccountRegistrationResponse(
    val registrationId: String,
    val boss: BossAccountRegistrationBossResponse,
    val store: BossAccountRegistrationStoreResponse
) : AuditingTimeResponse() {

    companion object {
        fun of(registration: Registration): BossAccountRegistrationResponse {
            val response = BossAccountRegistrationResponse(
                registrationId = registration.id,
                boss = BossAccountRegistrationBossResponse.of(registration.boss),
                store = BossAccountRegistrationStoreResponse.of(registration.store),
            )
            response.setAuditingTimeByDocument(registration)
            return response
        }
    }

}


data class BossAccountRegistrationBossResponse(
    val socialType: BossAccountSocialType,
    val name: String,
    val businessNumber: String
) {

    companion object {
        fun of(boss: RegistrationBossForm): BossAccountRegistrationBossResponse {
            return BossAccountRegistrationBossResponse(
                socialType = boss.socialInfo.socialType,
                name = boss.name,
                businessNumber = boss.businessNumber.getNumberWithSeparator()
            )
        }
    }

}


data class BossAccountRegistrationStoreResponse(
    val name: String,
    val categoriesIds: Set<String> = setOf(),
    val contactsNumber: String,
    val certificationPhotoUrl: String
) {

    companion object {
        fun of(store: RegistrationStoreForm): BossAccountRegistrationStoreResponse {
            return BossAccountRegistrationStoreResponse(
                name = store.name,
                categoriesIds = store.categoriesIds,
                contactsNumber = store.contactsNumber.getNumberWithSeparator(),
                certificationPhotoUrl = store.certificationPhotoUrl
            )
        }
    }

}
