package com.depromeet.threedollar.document.boss.document.registration

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.store.BossStore
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_registration_v1")
class Registration(
    val boss: RegistrationBossForm,
    val store: RegistrationStoreForm,
    var status: RegistrationStatus = RegistrationStatus.WAITING
) : BaseDocument() {

    fun toBossAccount(): BossAccount {
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
    val socialInfo: BossAccountSocialInfo,
    val name: String,
    val businessNumber: BusinessNumber
) {

    fun toBossAccount(): BossAccount {
        return BossAccount.of(
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
