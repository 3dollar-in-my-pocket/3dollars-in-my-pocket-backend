package com.depromeet.threedollar.domain.rds.user.domain.store

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class StoreStatisticsRepositoryTest(
    private val storeRepository: StoreRepository
) {

    @Nested
    inner class FindActiveStoresCountsTest {

        @Test
        fun 활성화된_전체_가게수를_조회한다() {
            // given
            val store1 = StoreCreator.createWithDefaultMenu(
                    userId = 10001L,
                    storeName = "가게 1"
            )
            val store2 = StoreCreator.createWithDefaultMenu(
                    userId = 10002L,
                    storeName = "가게 2"
            )
            storeRepository.saveAll(listOf(store1, store2))

            // when
            val counts = storeRepository.countAllActiveStores()

            // then
            assertThat(counts).isEqualTo(2)
        }

        @Test
        fun 메뉴없는_가게는_활성화된_전체_가게수에_포함되지_않는다() {
            // given
            val store = StoreCreator.create(
                    userId = 10001L,
                    storeName = "가게 1"
            )
            storeRepository.save(store)

            // when
            val counts = storeRepository.countAllActiveStores()

            // then
            assertThat(counts).isEqualTo(0)
        }

        @Test
        fun 삭제된_가게는_활성화된_전체_가게수에_포함되지_않는다() {
            // given
            val store = StoreCreator.createDeletedWithDefaultMenu(
                    userId = 10001L,
                    storeName = "가게 1"
            )
            storeRepository.save(store)

            // when
            val counts = storeRepository.countAllActiveStores()

            // then
            assertThat(counts).isEqualTo(0)
        }

    }

}
