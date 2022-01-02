package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.resolver.Coordinate;
import com.depromeet.threedollar.api.controller.MockMvcUtils;
import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveMyStoresV2Request;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveStoreGroupByCategoryV2Request;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByDistanceV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByReviewV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresCursorV2Response;
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

    StoreRetrieveMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<List<StoreWithVisitsAndDistanceResponse>> getNearStores(RetrieveNearStoresRequest request, Coordinate coordinate, Coordinate mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/near")
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()))
            .param("mapLatitude", String.valueOf(mapCoordinate.getLatitude()))
            .param("mapLongitude", String.valueOf(mapCoordinate.getLongitude()))
            .param("distance", String.valueOf(request.getDistance().getAvailableDistance() * 1000))
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

    ApiResponse<StoreDetailResponse> getStoreDetailInfo(RetrieveStoreDetailRequest request, Coordinate coordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/store")
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()))
            .param("storeId", String.valueOf(request.getStoreId()))
            .param("startDate", request.getStartDate() == null ? null : String.valueOf(request.getStartDate()));

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

    public ApiResponse<StoresCursorResponse> retrieveMyReportedStoreHistories(RetrieveMyStoresRequest request, String token, int expectedStatus) throws Exception {
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

    ApiResponse<StoresCursorV2Response> retrieveMyReportedStoreHistoriesV2(RetrieveMyStoresV2Request request, Coordinate coordinate, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/me")
            .header(HttpHeaders.AUTHORIZATION, token)
            .param("size", String.valueOf(request.getSize()))
            .param("cursor", request.getCursor() == null ? null : String.valueOf(request.getCursor()))
            .param("cachingTotalElements", request.getCachingTotalElements() == null ? null : String.valueOf(request.getCachingTotalElements()))
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()));

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
    ApiResponse<StoresGroupByDistanceV2Response> getStoresGroupByDistance(RetrieveStoreGroupByCategoryV2Request request, Coordinate coordinate, Coordinate mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/distance")
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()))
            .param("mapLatitude", String.valueOf(mapCoordinate.getLatitude()))
            .param("mapLongitude", String.valueOf(mapCoordinate.getLongitude()))
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
    ApiResponse<StoresGroupByReviewV2Response> getStoresGroupByReview(RetrieveStoreGroupByCategoryV2Request request, Coordinate coordinate, Coordinate mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/stores/review")
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()))
            .param("mapLatitude", String.valueOf(mapCoordinate.getLatitude()))
            .param("mapLongitude", String.valueOf(mapCoordinate.getLongitude()))
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

    ApiResponse<CheckExistStoresNearbyResponse> checkExistStoresNearby(CheckExistsStoresNearbyRequest request, Coordinate mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v1/stores/near/exists")
            .param("mapLatitude", String.valueOf(mapCoordinate.getLatitude()))
            .param("mapLongitude", String.valueOf(mapCoordinate.getLongitude()))
            .param("distance", String.valueOf(request.getDistance().getAvailableDistance() * 1000));

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
