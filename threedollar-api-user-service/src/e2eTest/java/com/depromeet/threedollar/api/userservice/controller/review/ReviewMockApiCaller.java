package com.depromeet.threedollar.api.userservice.controller.review;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.depromeet.threedollar.api.userservice.controller.MockMvcUtils;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.AddReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.RetrieveMyReviewsRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.request.UpdateReviewRequest;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewInfoResponse;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewsCursorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class ReviewMockApiCaller extends MockMvcUtils {

    ReviewMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<ReviewInfoResponse> addReview(AddReviewRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = post("/v2/store/review")
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

    ApiResponse<ReviewInfoResponse> updateReview(Long reviewId, UpdateReviewRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put("/v2/store/review/" + reviewId)
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

    ApiResponse<String> deleteReview(Long reviewId, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/v2/store/review/" + reviewId)
            .header(HttpHeaders.AUTHORIZATION, token);

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

    ApiResponse<ReviewsCursorResponse> retrieveMyReviewHistories(RetrieveMyReviewsRequest request, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v3/store/reviews/me")
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
