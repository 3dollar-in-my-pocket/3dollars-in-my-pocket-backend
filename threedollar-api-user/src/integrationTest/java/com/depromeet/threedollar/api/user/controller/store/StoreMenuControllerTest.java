package com.depromeet.threedollar.api.user.controller.store;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.core.mapper.user.menu.dto.response.MenuCategoryResponse;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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

@AutoConfigureMockMvc
@SpringBootTest
class StoreMenuControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("GET /api/v2/store/menu/categories")
    @Test
    void 활성화된_메뉴_카테고리의_정보들을_조회한다() throws Exception {
        // when & then
        ApiResponse<List<MenuCategoryResponse>> response = objectMapper.readValue(
            mockMvc.perform(get("/v2/store/menu/categories"))
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
