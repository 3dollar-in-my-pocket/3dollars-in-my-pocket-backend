package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.boss.api.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.boss.api.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.boss.api.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryCreator
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.document.common.document.ContactsNumber
import com.depromeet.threedollar.document.common.document.TimeInterval
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreServiceTest(
    private val bossStoreService: BossStoreService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossDeletedStoreRepository: BossDeletedStoreRepository
) {

    @AfterEach
    fun cleanUp() {
        bossStoreRepository.deleteAll()
        bossDeletedStoreRepository.deleteAll()
    }

    @Nested
    inner class UpdateBossStore {

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
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역"
                    )
                ),
                categoriesIds = categoriesIds
            )

            // when
            bossStoreService.updateBossStoreInfo(bossStore.id, request, bossStore.bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0]?.let {
                    assertThat(it.name).isEqualTo(request.name)
                    assertThat(it.imageUrl).isEqualTo(request.imageUrl)
                    assertThat(it.introduction).isEqualTo(request.introduction)
                    assertThat(it.contactsNumber).isEqualTo(ContactsNumber.of("010-1234-1234"))
                    assertThat(it.snsUrl).isEqualTo(request.snsUrl)
                    assertThat(it.menus).containsExactlyInAnyOrder(
                        BossStoreMenu(
                            name = "팥 붕어빵",
                            price = 1000,
                            imageUrl = "https://menu.png",
                            groupName = "붕어빵"
                        )
                    )
                    assertThat(it.appearanceDays).containsExactlyInAnyOrder(
                        BossStoreAppearanceDay(
                            dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                            openingHours = TimeInterval(LocalTime.of(8, 0), endTime = LocalTime.of(10, 0)),
                            locationDescription = "강남역"
                        )
                    )
                    assertThat(it.categoriesIds).containsExactlyInAnyOrderElementsOf(categoriesIds)
                }
            })
        }

        @Test
        fun `다른 사장님의 가게 정보를 수정할 수 없다`() {
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
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역"
                    )
                ),
                categoriesIds = categoriesIds
            )

            // when & then
            assertThatThrownBy { bossStoreService.updateBossStoreInfo(bossStore.id, request, "anotherBossId") }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class DeleteBossStoreByBossId {

        @Test
        fun `사장님 계정의 가게들을 삭제한다`() {
            // given
            val bossId = "bossId"
            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
            )
            bossStoreRepository.save(bossStore)

            // when
            bossStoreService.deleteBossStoreByBossId(bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertThat(bossStores).isEmpty()
        }

        @Test
        fun `사장님 계정의 가게들을 삭제할때 가게 정보가 BossDeletedStore 도큐먼트에 백업되서 저장된다`() {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")

            val bossId = "bossId"
            val name = "사장님 가게"
            val imageUrl = "https://image.png"
            val contactsNumber = ContactsNumber.of("010-1234-1234")
            val snsUrl = "https://instagram.com/test"
            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = name,
                imageUrl = imageUrl,
                contactsNumber = contactsNumber,
                snsUrl = snsUrl,
                categoriesIds = categoriesIds
            )
            bossStoreRepository.save(bossStore)

            // when
            bossStoreService.deleteBossStoreByBossId(bossId)

            // then
            val bossDeletedStores = bossDeletedStoreRepository.findAll()
            assertAll({
                assertThat(bossDeletedStores).hasSize(1)
                bossDeletedStores[0].let {
                    assertThat(it.bossId).isEqualTo(bossId)
                    assertThat(it.name).isEqualTo(name)
                    assertThat(it.imageUrl).isEqualTo(imageUrl)
                    assertThat(it.contactsNumber).isEqualTo(contactsNumber)
                    assertThat(it.snsUrl).isEqualTo(snsUrl)
                    assertThat(it.menus).isEqualTo(bossStore.menus)
                    assertThat(it.appearanceDays).isEqualTo(bossStore.appearanceDays)
                    assertThat(it.categoriesIds).isEqualTo(categoriesIds)

                    assertThat(it.backupInfo.bossStoreId).isEqualTo(bossStore.id)
                    assertThat(it.backupInfo.bossStoreCreatedAt).isEqualToIgnoringNanos(bossStore.createdAt)
                }
            })
        }

        @Test
        fun `다른 사장님의 가게를 삭제할 수 없다`() {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "사장님 가게"
            )
            bossStoreRepository.save(bossStore)

            // when & then
            bossStoreService.deleteBossStoreByBossId("anotherBossId")

            // then
            val bossStores = bossStoreRepository.findAll()
            assertThat(bossStores).hasSize(1)
        }
    }

}

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String
): Set<String> {
    return titles.asSequence()
        .map { bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id }
        .toSet()
}
