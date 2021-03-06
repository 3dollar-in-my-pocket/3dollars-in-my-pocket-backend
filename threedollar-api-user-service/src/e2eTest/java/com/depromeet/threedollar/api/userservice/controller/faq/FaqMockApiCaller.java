package com.depromeet.threedollar.api.userservice.controller.faq;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.core.service.mapper.commonservice.faq.dto.response.FaqCategoryResponse;
import com.depromeet.threedollar.api.core.service.service.commonservice.faq.dto.response.FaqResponse;
import com.depromeet.threedollar.api.userservice.MockMvcHelper;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FaqMockApiCaller extends MockMvcHelper {

    FaqMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    ApiResponse<List<FaqResponse>> retrieveFaqsByCategory(int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/faqs");

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

    ApiResponse<List<FaqResponse>> retrieveFaqsByCategory(FaqCategory category, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/faqs")
            .param("category", category.toString());

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

    ApiResponse<List<FaqCategoryResponse>> retrieveFaqCategories(int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/v2/faq/categories");

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
