package com.depromeet.threedollar.admin.controller.store

import com.depromeet.threedollar.admin.controller.ControllerTestUtils
import com.depromeet.threedollar.admin.service.store.dto.response.ReportedStoresResponse
import com.depromeet.threedollar.admin.service.store.dto.response.StoreInfoResponse
import com.depromeet.threedollar.admin.service.store.dto.response.StoresCursorResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.domain.user.domain.store.Store
import com.depromeet.threedollar.domain.user.domain.store.StoreCreator
import com.depromeet.threedollar.domain.user.domain.store.StoreRepository
import com.depromeet.threedollar.domain.user.domain.storedelete.DeleteReasonType
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequest
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequestRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get

internal class StoreControllerTest(
    private val storeRepository: StoreRepository,
    private val storeDeleteRequestRepository: StoreDeleteRequestRepository
) : ControllerTestUtils() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        storeDeleteRequestRepository.deleteAllInBatch()
        storeRepository.deleteAll()
    }

    @DisplayName("GET /admin/v1/stores/reported")
    @Test
    fun N개이상_삭제_요청된_가게들을_삭제요청이_많은것부터_조회한다_첫페이지() {
        // given
        val store1 = StoreCreator.createWithDefaultMenu(100L, "가게1")
        val store2 = StoreCreator.createWithDefaultMenu(101L, "가게2")
        val store3 = StoreCreator.createWithDefaultMenu(102L, "가게3")
        val store4 = StoreCreator.createWithDefaultMenu(103L, "가게4")

        storeRepository.saveAll(listOf(store1, store2, store3, store4))
        storeDeleteRequestRepository.saveAll(
            listOf(
                // store1: 4개의 삭제 요청
                StoreDeleteRequest.of(store1, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 101L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 102L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 103L, DeleteReasonType.OVERLAPSTORE),

                // store2: 3개의 삭제 요청
                StoreDeleteRequest.of(store2, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store2, 101L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store2, 102L, DeleteReasonType.OVERLAPSTORE),

                // store4: 2개의 삭제 요청
                StoreDeleteRequest.of(store4, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store4, 101L, DeleteReasonType.OVERLAPSTORE),
            )
        )

        val minCount = 2
        val size = 2
        val page = 1

        // when
        val response = objectMapper.readValue(mockMvc.get("/admin/v1/stores/reported") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("minCount", minCount.toString())
            param("size", size.toString())
            param("page", page.toString())
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<List<ReportedStoresResponse>>>() {})

        // then
        assertAll({
            assertThat(response.data).hasSize(2)
            assertReportedStoresResponse(response.data[0], store1, 4)
            assertReportedStoresResponse(response.data[1], store2, 3)
        })
    }

    @DisplayName("GET /admin/v1/stores/reported")
    @Test
    fun N개이상_삭제_요청된_가게들을_삭제요청이_많은것부터_조회한다_두번째_페이지() {
        // given
        val store1 = StoreCreator.createWithDefaultMenu(100L, "가게1")
        val store2 = StoreCreator.createWithDefaultMenu(101L, "가게2")
        val store3 = StoreCreator.createWithDefaultMenu(102L, "가게3")
        val store4 = StoreCreator.createWithDefaultMenu(103L, "가게4")

        storeRepository.saveAll(listOf(store1, store2, store3, store4))
        storeDeleteRequestRepository.saveAll(
            listOf(
                // store1: 4개의 삭제 요청
                StoreDeleteRequest.of(store1, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 101L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 102L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store1, 103L, DeleteReasonType.OVERLAPSTORE),

                // store2: 3개의 삭제 요청
                StoreDeleteRequest.of(store2, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store2, 101L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store2, 102L, DeleteReasonType.OVERLAPSTORE),

                // store4: 2개의 삭제 요청
                StoreDeleteRequest.of(store4, 100L, DeleteReasonType.OVERLAPSTORE),
                StoreDeleteRequest.of(store4, 101L, DeleteReasonType.OVERLAPSTORE),
            )
        )

        val minCount = 2
        val size = 2
        val page = 2

        // when
        val response = objectMapper.readValue(mockMvc.get("/admin/v1/stores/reported") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("minCount", minCount.toString())
            param("size", size.toString())
            param("page", page.toString())
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<List<ReportedStoresResponse>>>() {})

        // then
        assertAll({
            assertThat(response.data).hasSize(1)
            assertReportedStoresResponse(response.data[0], store4, 2)
        })
    }

    @DisplayName("GET /admin/v1/stores/reported")
    @Test
    fun N개이상_삭제_요청된_가게들을_조회시_minCount보다_요청수가_적은_가게들은_조회되지_않는다() {
        // given
        val store1 = StoreCreator.createWithDefaultMenu(100L, "가게1")

        storeRepository.save(store1)

        val minCount = 2
        val size = 2
        val page = 1

        // when
        val response = objectMapper.readValue(mockMvc.get("/admin/v1/stores/reported") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("minCount", minCount.toString())
            param("size", size.toString())
            param("page", page.toString())
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<List<ReportedStoresResponse>>>() {})

        // then
        assertThat(response.data).isEmpty()
    }

    private fun assertReportedStoresResponse(response: ReportedStoresResponse, store: Store, reportsCount: Long) {
        assertThat(response.storeId).isEqualTo(store.id)
        assertThat(response.storeName).isEqualTo(store.name)
        assertThat(response.latitude).isEqualTo(store.latitude)
        assertThat(response.longitude).isEqualTo(store.longitude)
        assertThat(response.type).isEqualTo(store.type)
        assertThat(response.rating).isEqualTo(store.rating)
        assertThat(response.reportsCount).isEqualTo(reportsCount)
    }

    @DisplayName("GET /admin/v1/stores/latest")
    @Test
    fun 최신순으로_스크롤_페이지네이션으로_가게를_조회한다_첫스크롤() {
        // given
        val store1 = StoreCreator.createWithDefaultMenu(100L, "가게1")
        val store2 = StoreCreator.createWithDefaultMenu(101L, "가게2")
        val store3 = StoreCreator.createWithDefaultMenu(102L, "가게3")
        val store4 = StoreCreator.createWithDefaultMenu(103L, "가게3")
        storeRepository.saveAll(listOf(store1, store2, store3, store4))

        val size = 2

        // when
        val response = objectMapper.readValue(mockMvc.get("/admin/v1/stores/latest") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("size", size.toString())
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<StoresCursorResponse>>() {})

        // then
        assertAll({
            assertThat(response.data.contents).hasSize(2)
            assertStoreInfoResponse(response.data.contents[0], store4)
            assertStoreInfoResponse(response.data.contents[1], store3)
            assertThat(response.data.nextCursor).isEqualTo(store3.id)
        })
    }

    @DisplayName("GET /admin/v1/store/latest")
    @Test
    fun 최신순으로_스크롤_페이지네이션으로_가게를_조회한다_마지막_스크롤() {
        // given
        val store1 = StoreCreator.createWithDefaultMenu(100L, "가게1")
        val store2 = StoreCreator.createWithDefaultMenu(101L, "가게2")
        val store3 = StoreCreator.createWithDefaultMenu(102L, "가게3")
        val store4 = StoreCreator.createWithDefaultMenu(103L, "가게3")
        storeRepository.saveAll(listOf(store1, store2, store3, store4))

        val size = 2
        val cursor = store3.id

        // when
        val response = objectMapper.readValue(mockMvc.get("/admin/v1/stores/latest") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            param("size", size.toString())
            param("cursor", cursor.toString())
        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
        }.andReturn().response.contentAsString, object : TypeReference<ApiResponse<StoresCursorResponse>>() {})

        // then
        assertAll({
            assertThat(response.data.contents).hasSize(2)
            assertStoreInfoResponse(response.data.contents[0], store2)
            assertStoreInfoResponse(response.data.contents[1], store1)
            assertThat(response.data.nextCursor).isEqualTo(-1)
        })
    }

    private fun assertStoreInfoResponse(storeInfoResponse: StoreInfoResponse, store: Store) {
        assertThat(storeInfoResponse.storeId).isEqualTo(store.id)
        assertThat(storeInfoResponse.storeName).isEqualTo(store.name)
        assertThat(storeInfoResponse.categories).isEqualTo(store.menuCategoriesSortedByCounts)
        assertThat(storeInfoResponse.latitude).isEqualTo(store.latitude)
        assertThat(storeInfoResponse.longitude).isEqualTo(store.longitude)
        assertThat(storeInfoResponse.rating).isEqualTo(store.rating)
    }

}
