package com.depromeet.threedollar.api.admin.service.user.store

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.admin.service.SetupAdminServiceTest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator

internal class AdminUserStoreServiceTest(
    private val adminUserStoreService: AdminUserStoreService,
    private val storeRepository: StoreRepository
) : SetupAdminServiceTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        storeRepository.deleteAll()
    }

    @Nested
    inner class DeleteUserStoreByAdminTest {

        @Test
        fun `관리자가 특정 가게를 강제로 삭제한다`() {
            // given
            val store = StoreWithMenuCreator.builder()
                .userId(10000L)
                .storeName("가게 이름")
                .latitude(36.0)
                .longitude(126.0)
                .rating(1.0)
                .build()
            storeRepository.save(store)

            // when
            adminUserStoreService.deleteStoreByForce(storeId = store.id)

            // then
            val stores = storeRepository.findAll()
            assertAll({
                assertThat(stores).hasSize(1)
                stores[0].let {
                    assertThat(it.status).isEqualTo(StoreStatus.FILTERED)
                    assertThat(it.isDeleted).isTrue
                    assertThat(it.id).isEqualTo(store.id)
                }
            })
        }

        @Test
        fun `관리자가 특정 가게를 강제로 삭제시 해당하는 가게가 없는경우 NotFoundException 에러가 발생한다`() {
            // when & then
            assertThatThrownBy { adminUserStoreService.deleteStoreByForce(storeId = -1L) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `관리자가 특정 가게를 강제로 삭제시 해당하는 가게가 유저에 의해 이미 삭제처리된 경우 NotFoundException 에러가 발생한다`() {
            // given
            val store = StoreWithMenuCreator.builder()
                .userId(10000L)
                .storeName("가게 이름")
                .latitude(36.0)
                .longitude(126.0)
                .rating(1.0)
                .status(StoreStatus.DELETED)
                .build()
            storeRepository.save(store)

            // when & then
            assertThatThrownBy { adminUserStoreService.deleteStoreByForce(storeId = store.id) }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `관리자가 특정 가게를 강제로 삭제시 해당하는 가게가 관리자에 의해 이미 삭제처리된 경우 NotFoundException 에러가 발생한다`() {
            // given
            val store = StoreWithMenuCreator.builder()
                .userId(10000L)
                .storeName("가게 이름")
                .latitude(36.0)
                .longitude(126.0)
                .rating(1.0)
                .status(StoreStatus.FILTERED)
                .build()
            storeRepository.save(store)

            // when & then
            assertThatThrownBy { adminUserStoreService.deleteStoreByForce(storeId = store.id) }.isInstanceOf(NotFoundException::class.java)
        }

    }

}
