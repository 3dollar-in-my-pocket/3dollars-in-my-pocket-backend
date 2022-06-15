package com.depromeet.threedollar.api.userservice.controller.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.core.service.userservice.menu.dto.response.MenuCategoryResponse;
import com.depromeet.threedollar.api.userservice.SetupControllerTest;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;

class StoreMenuControllerTest extends SetupControllerTest {

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
