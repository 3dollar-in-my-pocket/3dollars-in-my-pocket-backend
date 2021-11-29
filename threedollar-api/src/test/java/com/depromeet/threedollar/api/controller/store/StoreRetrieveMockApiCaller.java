package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.controller.MockMvcUtils;
import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.api.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreRetrieveMockApiCaller extends MockMvcUtils {

    public StoreRetrieveMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<List<StoreWithVisitsAndDistanceResponse>> getNearStores(RetrieveNearStoresRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/near")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("mapLatitude", String.valueOf(request.getMapLatitude()))
            .param("mapLongitude", String.valueOf(request.getMapLongitude()))
            .param("distance", String.valueOf(request.getDistance() * 1000))
            .param("category", request.getCategory() == null ? null : String.valueOf(request.getCategory()))
            .param("orderType", request.getOrderType() == null ? String.valueOf(StoreOrderType.DISTANCE_ASC) : String.valueOf(request.getOrderType()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<StoreDetailResponse> getStoreDetailInfo(RetrieveStoreDetailRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/store")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("storeId", String.valueOf(request.getStoreId()))
            .param("startDate", request.getStartDate() == null ? null : String.valueOf(request.getStartDate()))
            .param("endDate", request.getEndDate() == null ? null : String.valueOf(request.getEndDate()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<StoresScrollResponse> getMyStores(RetrieveMyStoresRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v3/stores/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .param("size", String.valueOf(request.getSize()))
            .param("cursor", request.getCursor() == null ? null : String.valueOf(request.getCursor()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<StoresScrollV2Response> getMyStoresV2(RetrieveMyStoresV2Request request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .param("size", String.valueOf(request.getSize()))
            .param("cursor", request.getCursor() == null ? null : String.valueOf(request.getCursor()))
            .param("cachingTotalElements", request.getCachingTotalElements() == null ? null : String.valueOf(request.getCachingTotalElements()))
            .param("latitude", request.getLatitude() == null ? null : String.valueOf(request.getLatitude()))
            .param("longitude", request.getLongitude() == null ? null : String.valueOf(request.getLongitude()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    @Deprecated
    public ApiResponse<StoresGroupByDistanceResponse> getStoresByDistance(RetrieveStoreGroupByCategoryRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/distance")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("mapLatitude", String.valueOf(request.getMapLatitude()))
            .param("mapLongitude", String.valueOf(request.getMapLongitude()))
            .param("category", String.valueOf(request.getCategory()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    @Deprecated
    public ApiResponse<StoresGroupByReviewResponse> getStoresByReview(RetrieveStoreGroupByCategoryRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/review")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("mapLatitude", String.valueOf(request.getMapLatitude()))
            .param("mapLongitude", String.valueOf(request.getMapLongitude()))
            .param("category", String.valueOf(request.getCategory()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
