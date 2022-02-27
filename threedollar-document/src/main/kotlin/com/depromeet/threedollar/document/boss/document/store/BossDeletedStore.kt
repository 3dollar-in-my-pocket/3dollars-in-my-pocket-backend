package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.ContactsNumber
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
    var categoriesIds: Set<String> = setOf()
) {

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
                menus = bossStore.menus.map { it.copy() }.toList(),
                appearanceDays = bossStore.appearanceDays.map { it.copy() }.toSet(),
                categoriesIds = bossStore.categoriesIds
            )
        }
    }

}


data class BackupBossStoreInfo(
    val bossStoreId: String,
    val bossStoreCreatedDateTime: LocalDateTime
) {

    companion object {
        fun of(bossStore: BossStore): BackupBossStoreInfo {
            return BackupBossStoreInfo(
                bossStoreId = bossStore.id,
                bossStoreCreatedDateTime = bossStore.createdDateTime
            )
        }
    }

}