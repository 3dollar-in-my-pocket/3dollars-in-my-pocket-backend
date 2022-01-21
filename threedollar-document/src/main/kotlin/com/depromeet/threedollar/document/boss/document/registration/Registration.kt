package com.depromeet.threedollar.document.boss.document.registration

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.common.document.BusinessNumber
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document

@Document("boss_registration_v1")
class Registration(
    val boss: RegistrationBossForm,
    val store: RegistrationStoreForm,
    val status: RegistrationStatus = RegistrationStatus.WAITING
) : BaseDocument() {

    fun addCategories(categories: List<String>) {
        this.store.addCategories(categories)
    }

}


data class RegistrationBossForm(
    val socialInfo: BossAccountSocialInfo,
    val name: String,
    val businessNumber: BusinessNumber
)


data class RegistrationStoreForm(
    val name: String,
    val categories: MutableList<String> = mutableListOf(),
    val contactsNumber: ContactsNumber,
    val certificationPhotoUrl: String
) {

    fun addCategories(categories: List<String>) {
        this.categories.addAll(categories)
    }

}
