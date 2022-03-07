package com.depromeet.threedollar.api.user.controller.storeimage;

import com.depromeet.threedollar.api.user.controller.MockMvcUtils;
import com.depromeet.threedollar.api.user.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

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
        return objectMapper.readValue(
            mockMvc.perform(get("/v2/store/" + storeId + "/images")
                .param("storeId", String.valueOf(storeId)))
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    ApiResponse<String> deleteStoreImage(Long imageId, String token, int expectedStatus) throws Exception {
        return objectMapper.readValue(
            mockMvc.perform(delete("/v2/store/image/".concat(String.valueOf(imageId)))
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(expectedStatus))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
