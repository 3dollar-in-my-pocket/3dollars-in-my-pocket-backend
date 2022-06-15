package com.depromeet.threedollar.api.userservice.controller.visit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.MockMvcUtils;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class VisitHistoryApiCaller extends MockMvcUtils {

    VisitHistoryApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<String> addVisitHistory(AddVisitHistoryRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/v2/store/visit")
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

    ApiResponse<VisitHistoriesCursorResponse> retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/store/visits/me")
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

}
