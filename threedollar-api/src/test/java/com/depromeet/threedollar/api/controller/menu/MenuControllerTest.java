package com.depromeet.threedollar.api.controller.menu;

import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.application.mapper.menu.dto.response.MenuCategoryResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends AbstractControllerTest {

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("GET /api/v2/store/menu/categories")
    @Test
    void 활성화된_메뉴_카테고리의_정보들을_조회한다() throws Exception {
        // given
        MockHttpServletRequestBuilder builder = get("/api/v2/store/menu/categories");

        // when
        ApiResponse<List<MenuCategoryResponse>> response = objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );

        // then
        assertAll(
            () -> assertThat(response.getData()).hasSize(MenuCategoryType.values().length),
            () -> assertThat(response.getData()).containsExactlyElementsOf(Arrays.stream(MenuCategoryType.values())
                .sorted(Comparator.comparing(MenuCategoryType::getDisplayOrder))
                .map(MenuCategoryResponse::of)
                .collect(Collectors.toList()))
        );
    }

}
