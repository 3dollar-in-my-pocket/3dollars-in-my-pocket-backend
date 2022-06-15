package com.depromeet.threedollar.api.userservice.controller.store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.MockMvcHelper;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreInfoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class StoreMockApiCaller extends MockMvcHelper {

    StoreMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<StoreInfoResponse> registerStore(RegisterStoreRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/v2/store")
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request));

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

    ApiResponse<StoreInfoResponse> updateStore(Long storeId, UpdateStoreRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/v2/store/" + storeId)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request));

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

    ApiResponse<StoreDeleteResponse> deleteStore(Long storeId, DeleteStoreRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/v2/store/" + storeId)
            .header(HttpHeaders.AUTHORIZATION, token)
            .param("deleteReasonType", String.valueOf(request.getDeleteReasonType()));

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
