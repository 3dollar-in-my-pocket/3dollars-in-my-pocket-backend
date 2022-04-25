package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument
import com.depromeet.threedollar.domain.mongo.common.domain.BusinessNumber
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_registration_v1")
class Registration(
    val boss: RegistrationBossForm,
    val store: RegistrationStoreForm,
    var status: RegistrationStatus
) : BaseDocument() {

    fun toBossAccount(): BossAccount {
        return this.boss.toBossAccount(this.id)
    }

    fun toBossStore(bossId: String): BossStore {
        return this.store.toBossStore(bossId)
    }

    fun approve() {
        this.status = RegistrationStatus.APPROVED
    }

    fun reject() {
        this.status = RegistrationStatus.REJECTED
    }

    companion object {
        fun of(
            boss: RegistrationBossForm,
            store: RegistrationStoreForm,
            status: RegistrationStatus = RegistrationStatus.WAITING
        ): Registration {
            return Registration(
                boss = boss,
                store = store,
                status = status
            )
        }
    }

}


data class RegistrationBossForm(
    val socialInfo: BossAccountSocialInfo,
    val name: String,
    val businessNumber: BusinessNumber
) {

    fun toBossAccount(registrationId: String): BossAccount {
        return BossAccount.of(
            bossId = registrationId,
            name = name,
            socialId = socialInfo.socialId,
            socialType = socialInfo.socialType,
            businessNumber = businessNumber
        )
    }

    companion object {
        fun of(
            socialId: String,
            socialType: BossAccountSocialType,
            name: String,
            businessNumber: String
        ): RegistrationBossForm {
            return RegistrationBossForm(
                socialInfo = BossAccountSocialInfo.of(socialId = socialId, socialType = socialType),
                name = name,
                businessNumber = BusinessNumber.of(businessNumber)
            )
        }
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

    companion object {
        fun of(
            name: String,
            categoriesIds: Set<String> = setOf(),
            contactsNumber: String,
            certificationPhotoUrl: String
        ): RegistrationStoreForm {
            return RegistrationStoreForm(
                name = name,
                categoriesIds = categoriesIds,
                contactsNumber = ContactsNumber.of(contactsNumber),
                certificationPhotoUrl = certificationPhotoUrl
            )
        }
    }

}
