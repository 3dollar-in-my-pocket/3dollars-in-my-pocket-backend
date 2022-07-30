package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.core.service.mapper.userservice.menu.dto.response.MenuCategoryResponse;
import com.depromeet.threedollar.api.userservice.ControllerTest;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

class StoreMenuControllerTest extends ControllerTest {

    @DisplayName("GET /api/v2/store/menu/categories")
    @Test
    void 유저_가게의_메뉴_카테고리_목록을_조회합니다() throws Exception {
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
            () -> assertThat(response.getData()).hasSize(UserMenuCategoryType.values().length),
            () -> assertThat(response.getData()).containsExactlyElementsOf(Arrays.stream(UserMenuCategoryType.values())
                .sorted(Comparator.comparing(UserMenuCategoryType::getDisplayOrder))
                .map(MenuCategoryResponse::of)
                .collect(Collectors.toList()))
        );
    }

}
