package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.MockMvcUtils;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailInfoRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreGroupByCategoryRequest;
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

    public ApiResponse<List<StoreInfoResponse>> getNearStores(RetrieveNearStoresRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/near")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("mapLatitude", String.valueOf(request.getMapLatitude()))
            .param("mapLongitude", String.valueOf(request.getMapLongitude()))
            .param("distance", String.valueOf(request.getDistance()));

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

    public ApiResponse<StoreDetailResponse> getStoreDetailInfo(RetrieveStoreDetailInfoRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/store")
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()))
            .param("storeId", String.valueOf(request.getStoreId()));

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
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .param("size", String.valueOf(request.getSize()))
            .param("cursor", request.getCursor() == null ? null : String.valueOf(request.getCursor()))
            .param("cachingTotalElements", request.getCachingTotalElements() == null ? null : String.valueOf(request.getCachingTotalElements()))
            .param("latitude", String.valueOf(request.getLatitude()))
            .param("longitude", String.valueOf(request.getLongitude()));

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
