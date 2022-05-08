package com.depromeet.threedollar.api.boss.controller

import java.time.LocalTime
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStore
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreAppearanceDayCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreMenuCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber

internal abstract class SetupBossStoreControllerTest : SetupBossAccountControllerTest() {

    @Autowired
    protected lateinit var bossStoreCategoryRepository: BossStoreCategoryRepository

    @Autowired
    protected lateinit var bossStoreRepository: BossStoreRepository

    @Autowired
    protected lateinit var bossStoreLocationRepository: BossStoreLocationRepository

    protected lateinit var bossStoreId: String
    protected lateinit var bossStore: BossStore

    @BeforeEach
    protected fun setupBossStore() {
        val category = BossStoreCategoryCreator.create("한식", 1)
        bossStoreCategoryRepository.save(category)
        bossStore = BossStoreCreator.create(
            bossId = bossId,
            name = "사장님 가게",
            imageUrl = "https://image.png",
            introduction = "사장님 가게에 대한 소개",
            snsUrl = "https://instagram.com/foodtruck",
            contactsNumber = ContactsNumber.of("010-1234-1234"),
            menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
            appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                dayOfTheWeek = DayOfTheWeek.FRIDAY,
                startTime = LocalTime.of(8, 0),
                endTime = LocalTime.of(10, 0),
                locationDescription = "강남역")
            ),
            categoriesIds = setOf(category.id)
        )
        bossStoreRepository.save(bossStore)
        bossStoreId = bossStore.id
    }

    override fun cleanup() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreLocationRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

}
