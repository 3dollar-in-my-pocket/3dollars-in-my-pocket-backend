package com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.RegistrationStoreForm

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
                categories = store.categoriesIds.asSequence()
                    .mapNotNull { categoryId -> bossStoreCategoryMap[categoryId]?.name }
                    .toSet(),
                contactsNumber = store.contactsNumber.getNumberWithSeparator(),
                certificationPhotoUrl = store.certificationPhotoUrl
            )
        }
    }

}
