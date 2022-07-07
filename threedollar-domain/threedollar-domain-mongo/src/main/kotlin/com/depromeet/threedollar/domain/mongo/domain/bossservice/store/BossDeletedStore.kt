package com.depromeet.threedollar.domain.mongo.domain.bossservice.store

import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("boss_deleted_store_v1")
class BossDeletedStore(
    val backupInfo: BackupBossStoreInfo,
    val bossId: String,
    var name: String,
    var imageUrl: String?,
    var introduction: String?,
    var contactsNumber: ContactsNumber?,
    var snsUrl: String?,
    var menus: List<BossStoreMenu> = listOf(),
    var appearanceDays: Set<BossStoreAppearanceDay> = setOf(),
    var categoriesIds: Set<String> = setOf(),
) : BaseDocument() {

    companion object {
        fun of(bossStore: BossStore): BossDeletedStore {
            return BossDeletedStore(
                backupInfo = BackupBossStoreInfo.of(bossStore),
                bossId = bossStore.bossId,
                name = bossStore.name,
                imageUrl = bossStore.imageUrl,
                introduction = bossStore.introduction,
                contactsNumber = bossStore.contactsNumber?.copy(),
                snsUrl = bossStore.snsUrl,
                menus = bossStore.menus.asSequence()
                    .map { menu -> menu.copy() }
                    .toList(),
                appearanceDays = bossStore.appearanceDays.asSequence()
                    .map { bossStoreAppearanceDay -> bossStoreAppearanceDay.copy() }
                    .toSet(),
                categoriesIds = bossStore.categoriesIds
            )
        }
    }

}


data class BackupBossStoreInfo(
    val bossStoreId: String,
    val bossStoreCreatedAt: LocalDateTime,
) {

    companion object {
        fun of(bossStore: BossStore): BackupBossStoreInfo {
            return BackupBossStoreInfo(
                bossStoreId = bossStore.id,
                bossStoreCreatedAt = bossStore.createdAt
            )
        }
    }

}
