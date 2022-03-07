package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_registration_v1")
class Registration(
    val boss: RegistrationBossForm,
    val store: RegistrationStoreForm,
    var status: RegistrationStatus = RegistrationStatus.WAITING
) : BaseDocument() {

    fun toBossAccount(): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        return this.boss.toBossAccount()
    }

    fun toBossStore(bossAccountId: String): BossStore {
        return this.store.toBossStore(bossAccountId)
    }

    fun approve() {
        this.status = RegistrationStatus.APPROVED
    }

    fun reject() {
        this.status = RegistrationStatus.REJECTED
    }

}


data class RegistrationBossForm(
    val socialInfo: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo,
    val name: String,
    val businessNumber: BusinessNumber
) {

    fun toBossAccount(): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        return com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount.of(
            name = name,
            socialId = socialInfo.socialId,
            socialType = socialInfo.socialType,
            businessNumber = businessNumber
        )
    }

}


data class RegistrationStoreForm(
        val name: String,
        val categoriesIds: Set<String> = setOf(),
        val contactsNumber: ContactsNumber,
        val certificationPhotoUrl: String
) {

    fun toBossStore(bossId: String): BossStore {
        return BossStore.of(
            bossId = bossId,
            name = name,
            categoriesIds = categoriesIds,
            contactsNumber = contactsNumber
        )
    }

}
