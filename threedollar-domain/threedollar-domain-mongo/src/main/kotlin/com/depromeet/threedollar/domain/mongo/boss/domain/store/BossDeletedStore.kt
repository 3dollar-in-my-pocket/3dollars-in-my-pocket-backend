package com.depromeet.threedollar.domain.mongo.boss.domain.store

import java.time.LocalDateTime
import org.springframework.data.mongodb.core.mapping.Document
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument

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
                menus = bossStore.menus.asSequence().map { it.copy() }.toList(),
                appearanceDays = bossStore.appearanceDays.asSequence().map { it.copy() }.toSet(),
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
