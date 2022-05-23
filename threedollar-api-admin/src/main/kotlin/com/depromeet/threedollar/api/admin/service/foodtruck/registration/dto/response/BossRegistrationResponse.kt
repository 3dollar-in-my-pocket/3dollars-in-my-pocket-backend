package com.depromeet.threedollar.api.admin.service.foodtruck.registration.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.RegistrationStoreForm

data class BossAccountRegistrationResponse(
    val registrationId: String,
    val boss: BossAccountRegistrationBossResponse,
    val store: BossAccountRegistrationStoreResponse,
) : AuditingTimeResponse() {

    companion object {
        fun of(bossRegistration: BossRegistration, bossStoreCategoryMap: Map<String, BossStoreCategory>): BossAccountRegistrationResponse {
            val response = BossAccountRegistrationResponse(
                registrationId = bossRegistration.id,
                boss = BossAccountRegistrationBossResponse.of(bossRegistration.boss),
                store = BossAccountRegistrationStoreResponse.of(bossRegistration.store, bossStoreCategoryMap),
            )
            response.setAuditingTimeByDocument(bossRegistration)
            return response
        }
    }

}


data class BossAccountRegistrationBossResponse(
    val socialType: BossAccountSocialType,
    val name: String,
    val businessNumber: String,
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
    val categories: Set<String> = setOf(),
    val contactsNumber: String,
    val certificationPhotoUrl: String,
) {

    companion object {
        fun of(store: RegistrationStoreForm, bossStoreCategoryMap: Map<String, BossStoreCategory>): BossAccountRegistrationStoreResponse {
            return BossAccountRegistrationStoreResponse(
                name = store.name,
                categories = store.categoriesIds.mapNotNull { bossStoreCategoryMap[it]?.name }.toSet(),
                contactsNumber = store.contactsNumber.getNumberWithSeparator(),
                certificationPhotoUrl = store.certificationPhotoUrl
            )
        }
    }

}
