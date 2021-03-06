package com.depromeet.threedollar.api.bossservice.service.store

import com.depromeet.threedollar.api.bossservice.SetupBossAccountIntegrationTest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossDeletedStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDay
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDayFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenu
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenuFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import java.time.LocalTime

internal class BossStoreSetupBossAccountServiceTest(
    private val bossStoreService: BossStoreService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossDeletedStoreRepository: BossDeletedStoreRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) : SetupBossAccountIntegrationTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreRepository.deleteAll()
        bossDeletedStoreRepository.deleteAll()
        bossStoreCategoryCacheRepository.cleanCache()
    }

    @Nested
    inner class UpdateBossStoreTest {

        @Test
        fun `????????? ????????? ????????? ????????????`() {
            // given
            val categoriesIds = createMockCategory(bossStoreCategoryRepository, "??????", "??????", "??????")
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
            )
            bossStoreRepository.save(bossStore)

            val request = UpdateBossStoreInfoRequest(
                name = "?????? ??? ??????",
                imageUrl = "https://after.png",
                introduction = "?????? ??? ??????",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "??? ?????????", price = 1000, imageUrl = "https://menu-bungeoppang.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "?????????"
                    )
                ),
                categoriesIds = categoriesIds
            )

            // when
            bossStoreService.updateBossStoreInfo(bossStore.id, request, bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0]?.let {
                    assertBossStore(
                        bossStore = bossStores[0],
                        bossId = bossId,
                        name = request.name,
                        imageUrl = request.imageUrl,
                        introduction = request.introduction,
                        contactsNumber = ContactsNumber.of("010-1234-1234"),
                        snsUrl = request.snsUrl,
                        categoriesIds = categoriesIds,
                        menus = listOf(
                            BossStoreMenuFixture.create(
                                name = "??? ?????????",
                                price = 1000,
                                imageUrl = "https://menu-bungeoppang.png"
                            )),
                        appearanceDays = setOf(
                            BossStoreAppearanceDayFixture.create(
                                dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                                startTime = LocalTime.of(8, 0),
                                endTime = LocalTime.of(10, 0),
                                locationDescription = "?????????"
                            ))
                    )
                }
            })
        }

        @Test
        fun `?????? ???????????? ?????? ????????? ????????? ??? ??????`() {
            // given
            val categoriesIds = createMockCategory(bossStoreCategoryRepository, "??????", "??????")
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
            )
            bossStoreRepository.save(bossStore)

            val request = UpdateBossStoreInfoRequest(
                name = "?????? ??? ??????",
                imageUrl = "https://after.png",
                introduction = "?????? ??? ??????",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "??? ?????????", price = 1000, imageUrl = "https://store-menu.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "?????????"
                    )
                ),
                categoriesIds = categoriesIds
            )

            // when & then
            assertThatThrownBy { bossStoreService.updateBossStoreInfo(bossStore.id, request, "anotherBossId") }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class PatchBossStoreTest {

        @Test
        fun `?????? ??????????????? ???????????? ?????? ???????????? ???????????? ?????????`() {
            // given
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                menus = listOf(BossStoreMenuFixture.create(name = "??????", price = 1000, imageUrl = "https://menu-image.png")),
                appearanceDays = setOf(BossStoreAppearanceDayFixture.create(dayOfTheWeek = DayOfTheWeek.FRIDAY, startTime = LocalTime.of(8, 0), endTime = LocalTime.of(10, 0))),
                categoriesIds = setOf("???????????? 1")
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest()

            // when
            bossStoreService.patchBossStoreInfo(bossStore.id, request, bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                assertBossStore(
                    bossStore = bossStores[0],
                    bossId = bossId,
                    name = bossStore.name,
                    imageUrl = bossStore.imageUrl,
                    introduction = bossStore.introduction,
                    contactsNumber = bossStore.contactsNumber,
                    snsUrl = request.snsUrl,
                    categoriesIds = bossStore.categoriesIds,
                    menus = bossStore.menus,
                    appearanceDays = bossStore.appearanceDays
                )
            })
        }

        @Test
        fun `?????? ????????? ????????? ????????????`() {
            // given
            val newName = "????????? ?????? ??????"
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest(
                name = newName
            )

            // when
            bossStoreService.patchBossStoreInfo(bossStore.id, request, bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                assertBossStore(
                    bossStore = bossStores[0],
                    name = newName,
                    bossId = bossId,
                    imageUrl = bossStore.imageUrl,
                    introduction = bossStore.introduction,
                    contactsNumber = bossStore.contactsNumber,
                    snsUrl = request.snsUrl,
                    categoriesIds = bossStore.categoriesIds,
                    menus = bossStore.menus,
                    appearanceDays = bossStore.appearanceDays
                )
            })
        }

        @Test
        fun `????????? ????????? ?????? ????????? ????????????`() {
            // given
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                menus = listOf(BossStoreMenuFixture.create(name = "??????", price = 1000, imageUrl = "https://menu0.png"))
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest(
                menus = listOf(
                    MenuRequest(name = "??? ?????????", price = 1000, imageUrl = "https://menu1.png"),
                    MenuRequest(name = "????????? ?????????", price = 1000, imageUrl = "https://menu2.png")
                )
            )

            // when
            bossStoreService.patchBossStoreInfo(bossStore.id, request, bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0].let {
                    assertThat(it.menus).hasSize(2)
                    assertThat(it.menus[0].name).isEqualTo("??? ?????????")
                    assertThat(it.menus[1].name).isEqualTo("????????? ?????????")
                }
            })
        }

        @Test
        fun `?????? ???????????? ??? ???????????? ????????????, ?????? ????????? ????????????`() {
            // given
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                menus = listOf(BossStoreMenuFixture.create(name = "??????", price = 1000, imageUrl = "https://menu-image.png")),
                appearanceDays = setOf(BossStoreAppearanceDayFixture.create(dayOfTheWeek = DayOfTheWeek.FRIDAY)),
                categoriesIds = setOf("???????????? 1")
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest(
                menus = listOf(),
                categoriesIds = setOf(),
                appearanceDays = setOf()
            )

            // when
            bossStoreService.patchBossStoreInfo(bossStore.id, request, bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                bossStores[0].let {
                    assertThat(it.menus).isEmpty()
                    assertThat(it.categoriesIds).isEmpty()
                    assertThat(it.appearanceDays).isEmpty()
                }
            })
        }

    }

    @Nested
    inner class DeleteBossStoreByBossIdTest {

        @Test
        fun `????????? ????????? ???????????? ????????????`() {
            // given
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
            )
            bossStoreRepository.save(bossStore)

            // when
            bossStoreService.deleteBossStoreByBossId(bossId)

            // then
            val bossStores = bossStoreRepository.findAll()
            assertThat(bossStores).isEmpty()
        }

        @Test
        fun `????????? ????????? ???????????? ???????????? ?????? ????????? BossDeletedStore ??????????????? ???????????? ????????????`() {
            // given
            val categoriesIds = createMockCategory(bossStoreCategoryRepository, "??????", "??????")

            val name = "????????? ??????"
            val imageUrl = "https://store-image.png"
            val contactsNumber = ContactsNumber.of("010-1234-1234")
            val snsUrl = "https://instagram.com/test"
            val bossStore = BossStoreFixture.create(
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
                assertBossDeletedStore(
                    bossDeletedStore = bossDeletedStores[0],
                    bossId = bossId,
                    name = name,
                    imageUrl = imageUrl,
                    contactsNumber = contactsNumber,
                    snsUrl = snsUrl,
                    menus = bossStore.menus,
                    appearanceDays = bossStore.appearanceDays,
                    categoriesIds = categoriesIds,
                    backUpBossStoreId = bossStore.id,
                    backUpBossStoreCreatedAt = bossStore.createdAt
                )
            })
        }

        @Test
        fun `?????? ???????????? ????????? ????????? ??? ??????`() {
            // given
            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????"
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

private fun createMockCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String,
): Set<String> {
    return titles.asSequence()
        .map { title -> bossStoreCategoryRepository.save(BossStoreCategoryFixture.create(title)).id }
        .toSet()
}


/**
 * AssertionsHelper
 */
private fun assertBossStore(
    bossStore: BossStore,
    bossId: String? = null,
    name: String? = null,
    imageUrl: String? = null,
    introduction: String? = null,
    contactsNumber: ContactsNumber? = null,
    snsUrl: String? = null,
    categoriesIds: Set<String>? = null,
    menus: List<BossStoreMenu>? = null,
    appearanceDays: Set<BossStoreAppearanceDay>? = null,
) {
    assertAll({
        bossId?.let { assertThat(bossStore.bossId).isEqualTo(it) }
        name?.let { assertThat(bossStore.name).isEqualTo(it) }
        imageUrl?.let { assertThat(bossStore.imageUrl).isEqualTo(it) }
        introduction?.let { assertThat(bossStore.introduction).isEqualTo(it) }
        contactsNumber?.let { assertThat(bossStore.contactsNumber).isEqualTo(it) }
        snsUrl?.let { assertThat(bossStore.snsUrl).isEqualTo(it) }
        categoriesIds?.let { assertThat(bossStore.categoriesIds).containsExactlyInAnyOrderElementsOf(it) }
        menus?.let { assertThat(bossStore.menus).containsExactlyInAnyOrderElementsOf(it) }
        appearanceDays?.let { assertThat(bossStore.appearanceDays).containsExactlyInAnyOrderElementsOf(it) }
    })
}

private fun assertBossDeletedStore(
    bossDeletedStore: BossDeletedStore,
    backUpBossStoreId: String? = null,
    backUpBossStoreCreatedAt: LocalDateTime? = null,
    bossId: String? = null,
    name: String? = null,
    imageUrl: String? = null,
    introduction: String? = null,
    contactsNumber: ContactsNumber? = null,
    snsUrl: String? = null,
    categoriesIds: Set<String>? = null,
    menus: List<BossStoreMenu>? = null,
    appearanceDays: Set<BossStoreAppearanceDay>? = null,
) {
    assertAll({
        backUpBossStoreId?.let { assertThat(bossDeletedStore.backupInfo.bossStoreId).isEqualTo(it) }
        backUpBossStoreCreatedAt?.let { assertThat(bossDeletedStore.backupInfo.bossStoreCreatedAt).isEqualToIgnoringNanos(it) }
        bossId?.let { assertThat(bossDeletedStore.bossId).isEqualTo(it) }
        name?.let { assertThat(bossDeletedStore.name).isEqualTo(it) }
        imageUrl?.let { assertThat(bossDeletedStore.imageUrl).isEqualTo(it) }
        introduction?.let { assertThat(bossDeletedStore.introduction).isEqualTo(it) }
        contactsNumber?.let { assertThat(bossDeletedStore.contactsNumber).isEqualTo(it) }
        snsUrl?.let { assertThat(bossDeletedStore.snsUrl).isEqualTo(it) }
        categoriesIds?.let { assertThat(bossDeletedStore.categoriesIds).containsExactlyInAnyOrderElementsOf(it) }
        menus?.let { assertThat(bossDeletedStore.menus).containsExactlyInAnyOrderElementsOf(it) }
        appearanceDays?.let { assertThat(bossDeletedStore.appearanceDays).containsExactlyInAnyOrderElementsOf(it) }
    })
}
