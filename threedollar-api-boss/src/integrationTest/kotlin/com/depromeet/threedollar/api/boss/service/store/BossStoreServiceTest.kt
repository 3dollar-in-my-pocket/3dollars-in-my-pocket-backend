package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.api.boss.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.*
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalTime

@SpringBootTest
internal class BossStoreServiceOneTest(
    private val bossStoreService: BossStoreService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossDeletedStoreRepository: BossDeletedStoreRepository
) : FunSpec({

    afterEach {
        withContext(Dispatchers.IO) {
            bossStoreRepository.deleteAll()
            bossDeletedStoreRepository.deleteAll()
        }
    }

    context("사장님 자신의 가게 정보를 수정한다") {
        test("사장님이 자신의 가게 정보를 수정하면 DB에 수정된 내용이 반영된다") {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "사장님 가게",
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

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
            withContext(Dispatchers.IO) {
                bossStoreService.updateBossStoreInfo(bossStore.id, request, bossStore.bossId)
            }

            // then
            val bossStores = bossStoreRepository.findAll()
            bossStores shouldHaveSize 1
            bossStores[0].also {
                it.name shouldBe request.name
                it.imageUrl shouldBe request.imageUrl
                it.introduction shouldBe request.introduction
                it.contactsNumber shouldBe ContactsNumber.of("010-1234-1234")
                it.snsUrl shouldBe request.snsUrl
                it.menus shouldContainExactlyInAnyOrder (listOf(
                    BossStoreMenu(
                        name = "팥 붕어빵",
                        price = 1000,
                        imageUrl = "https://menu.png",
                        groupName = "붕어빵"
                    )
                ))
                it.appearanceDays shouldContainExactlyInAnyOrder (listOf(
                    BossStoreAppearanceDay(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        openingHours = TimeInterval(LocalTime.of(8, 0), endTime = LocalTime.of(10, 0)),
                        locationDescription = "강남역"
                    )
                ))
                it.categoriesIds shouldContainExactlyInAnyOrder (categoriesIds)
            }
        }

        test("다른 사장님의 가게 정보를 수정하려는 경우 NotFound 에러가 발생한다") {
            // given
            val categoriesIds = createCategory(bossStoreCategoryRepository, "한식", "일식")
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "사장님 가게",
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

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
            shouldThrowExactly<NotFoundException> {
                bossStoreService.updateBossStoreInfo(bossStore.id, request, "anotherBossId")
            }
        }
    }

    context("사장님 가게를 삭제한다") {
        test("사장님 가게를 삭제하는 경우 DB에서 해당 가게 데이터가 삭제된다") {
            // given
            val bossId = "bossId"
            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            // when
            withContext(Dispatchers.IO) {
                bossStoreService.deleteBossStoreByBossId(bossId)
            }

            // then
            val bossStores = bossStoreRepository.findAll()
            bossStores shouldHaveSize 0
        }

        test("사장님 가게를 삭제하는 경우 BossDeletedStore 테이블에 해당 가게 정보가 백업되서 저장된다") {
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
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            // when
            withContext(Dispatchers.IO) {
                bossStoreService.deleteBossStoreByBossId(bossId)
            }

            // then
            val bossDeletedStores = bossDeletedStoreRepository.findAll()
            bossDeletedStores shouldHaveSize 1
            bossDeletedStores[0].also {
                it.bossId shouldBe bossId
                it.name shouldBe name
                it.imageUrl shouldBe imageUrl
                it.contactsNumber shouldBe contactsNumber
                it.snsUrl shouldBe snsUrl
                it.menus shouldBe bossStore.menus
                it.appearanceDays shouldBe bossStore.appearanceDays
                it.categoriesIds shouldBe categoriesIds
                it.backupInfo.bossStoreId shouldBe bossStore.id
            }
        }

        test("다른 사장님의 가게를 삭제할 수 없다") {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "사장님 가게"
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            // when & then
            withContext(Dispatchers.IO) {
                bossStoreService.deleteBossStoreByBossId("anotherBossId")
            }

            // then
            val bossStores = bossStoreRepository.findAll()
            bossStores shouldHaveSize 1
        }
    }

})

private fun createCategory(
    bossStoreCategoryRepository: BossStoreCategoryRepository,
    vararg titles: String
): Set<String> {
    return titles.asSequence()
        .map { bossStoreCategoryRepository.save(BossStoreCategoryCreator.create(it)).id }
        .toSet()
}
