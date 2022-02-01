package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.boss.api.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.boss.api.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryCreator
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreAppearanceDay
import com.depromeet.threedollar.document.boss.document.store.BossStoreCreator
import com.depromeet.threedollar.document.boss.document.store.BossStoreMenu
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.TimeInterval
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreServiceTest(
    private val bossStoreService: BossStoreService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @AfterEach
    fun cleanUp() {
        bossStoreRepository.deleteAll()
    }

    @Test
    fun `사장님 가게의 정보를 수정한다`() {
        // given
        val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "사장님 가게",
        )
        bossStoreRepository.save(bossStore)

        val request = UpdateBossStoreInfoRequest(
            name = "변경 후 이름",
            imageUrl = "https://after.png",
            introduction = "변경 후 소개",
            contactsNumber = "010-1234-1234",
            snsUrl = "https://instagram.com",
            menus = listOf(
                MenuRequest(name = "팥 붕어빵", price = 1000, imageUrl = "https://menu.png", groupName = "붕어빵")
            ),
            appearanceDays = setOf(
                AppearanceDayRequest(day = DayOfTheWeek.WEDNESDAY, startTime = LocalTime.of(8, 0), endTime = LocalTime.of(10, 0), locationDescription = "강남역")
            ),
            categoriesIds = categoriesIds
        )

        // when
        bossStoreService.updateBossStoreInfo(bossStore.id, request, bossStore.bossId)

        // then
        val bossStores = bossStoreRepository.findAll()
        assertThat(bossStores).hasSize(1)
        bossStores[0]?.let {
            assertThat(it.name).isEqualTo(request.name)
            assertThat(it.imageUrl).isEqualTo(request.imageUrl)
            assertThat(it.introduction).isEqualTo(request.introduction)
            assertThat(it.contactsNumber).isEqualTo(ContactsNumber.of("010-1234-1234"))
            assertThat(it.snsUrl).isEqualTo(request.snsUrl)
            assertThat(it.menus).containsExactlyInAnyOrder(BossStoreMenu(name = "팥 붕어빵", price = 1000, imageUrl = "https://menu.png", groupName = "붕어빵"))
            assertThat(it.appearanceDays).containsExactlyInAnyOrder(BossStoreAppearanceDay(day = DayOfTheWeek.WEDNESDAY, openTime = TimeInterval(LocalTime.of(8, 0), endTime = LocalTime.of(10, 0)), locationDescription = "강남역"))
            assertThat(it.categoriesIds).containsExactlyInAnyOrderElementsOf(categoriesIds)
        }
    }

}

private fun createCategory(bossStoreCategoryRepository: BossStoreCategoryRepository, vararg titles: String): Set<String> {
    return titles.map {
        bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id
    }.toSet()
}
