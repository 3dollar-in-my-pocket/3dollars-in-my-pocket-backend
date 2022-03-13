package com.depromeet.threedollar.api.user.controller.store;

import com.depromeet.threedollar.api.user.controller.MockMvcUtils;
import com.depromeet.threedollar.api.user.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.api.user.service.store.dto.type.UserStoreOrderType;
import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
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

    ApiResponse<List<StoreWithVisitsAndDistanceResponse>> getNearStores(RetrieveNearStoresRequest request, CoordinateValue coordinate, CoordinateValue mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/stores/near")
            .param("latitude", String.valueOf(coordinate.getLatitude()))
            .param("longitude", String.valueOf(coordinate.getLongitude()))
            .param("mapLatitude", String.valueOf(mapCoordinate.getLatitude()))
            .param("mapLongitude", String.valueOf(mapCoordinate.getLongitude()))
            .param("distance", String.valueOf(request.getDistance().getAvailableDistance() * 1000))
            .param("category", request.getCategory() == null ? null : String.valueOf(request.getCategory()))
            .param("orderType", request.getOrderType() == null ? String.valueOf(UserStoreOrderType.DISTANCE_ASC) : String.valueOf(request.getOrderType()));

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

    ApiResponse<StoreDetailResponse> getStoreDetailInfo(RetrieveStoreDetailRequest request, CoordinateValue coordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/store")
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
        MockHttpServletRequestBuilder builder = get("/v3/stores/me")
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

    ApiResponse<CheckExistStoresNearbyResponse> checkExistStoresNearby(CheckExistsStoresNearbyRequest request, CoordinateValue mapCoordinate, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v1/stores/near/exists")
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
