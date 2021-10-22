package com.depromeet.threedollar.domain.domain.store

import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType
import com.depromeet.threedollar.domain.domain.menu.MenuCreator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class StoreStaticsRepositoryTest(
    private val storeRepository: StoreRepository
) {

    @Nested
    inner class FindActiveStoresCounts {

        @Test
        fun 활성화된_전체_가게수를_조회한다() {
            // given
            val store1 = StoreCreator.create(10001L, "가게 1")
            store1.addMenus(listOf(MenuCreator.create(store1, "메뉴 1", "가격 1", MenuCategoryType.DALGONA)))

            val store2 = StoreCreator.create(10002L, "가게 2")
            store2.addMenus(listOf(MenuCreator.create(store2, "메뉴 1", "가격 1", MenuCategoryType.DALGONA)))

            storeRepository.saveAll(listOf(store1, store2))

            // when
            val counts = storeRepository.findActiveStoresCounts()

            // then
            assertThat(counts).isEqualTo(2)
        }

        @Test
        fun 메뉴없는_가게는_활성화된_전체_가게수에_포함되지_않는다() {
            // given
            val store = StoreCreator.create(10001L, "가게 1")

            storeRepository.save(store)

            // when
            val counts = storeRepository.findActiveStoresCounts()

            // then
            assertThat(counts).isEqualTo(0)
        }

        @Test
        fun 삭제된_가게는_활성화된_전체_가게수에_포함되지_않는다() {
            // given
            val store = StoreCreator.create(10001L, "가게 1")
            store.addMenus(listOf(MenuCreator.create(store, "메뉴 1", "가격 1", MenuCategoryType.DALGONA)))
            store.delete()

            storeRepository.save(store)

            // when
            val counts = storeRepository.findActiveStoresCounts()

            // then
            assertThat(counts).isEqualTo(0)
        }

    }

}
