package com.depromeet.threedollar.api.admin.service.boss.registration.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationBossForm
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStoreForm

data class BossAccountRegistrationResponse(
    val registrationId: String,
    val boss: BossAccountRegistrationBossResponse,
    val store: BossAccountRegistrationStoreResponse
) : AuditingTimeResponse() {

    companion object {
        fun of(registration: Registration, bossStoreCategoryMap: Map<String, BossStoreCategory>): BossAccountRegistrationResponse {
            val response = BossAccountRegistrationResponse(
                registrationId = registration.id,
                boss = BossAccountRegistrationBossResponse.of(registration.boss),
                store = BossAccountRegistrationStoreResponse.of(registration.store, bossStoreCategoryMap),
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
    val categories: Set<String> = setOf(),
    val contactsNumber: String,
    val certificationPhotoUrl: String
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