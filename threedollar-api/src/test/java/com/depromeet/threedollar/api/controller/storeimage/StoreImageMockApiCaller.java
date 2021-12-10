package com.depromeet.threedollar.api.controller.storeimage;

import com.depromeet.threedollar.api.controller.MockMvcUtils;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreImageMockApiCaller extends MockMvcUtils {

    StoreImageMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<List<StoreImageResponse>> getStoreImages(Long storeId, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/store/" + storeId + "/images")
            .param("storeId", String.valueOf(storeId));

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

    ApiResponse<String> deleteStoreImage(Long imageId, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = delete("/api/v2/store/image/".concat(String.valueOf(imageId)))
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

}
