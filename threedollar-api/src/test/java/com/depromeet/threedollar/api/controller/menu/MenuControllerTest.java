package com.depromeet.threedollar.api.controller.menu;

import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.application.mapper.menu.dto.response.MenuCategoryResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.menu.MenuCategoryType.*;
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
            () -> assertThat(response.getData()).hasSize(15),
            () -> assertMenuCategoryResponse(response.getData().get(0), DALGONA),
            () -> assertMenuCategoryResponse(response.getData().get(1), BUNGEOPPANG),
            () -> assertMenuCategoryResponse(response.getData().get(2), HOTTEOK),
            () -> assertMenuCategoryResponse(response.getData().get(3), TAKOYAKI),
            () -> assertMenuCategoryResponse(response.getData().get(4), GYERANPPANG),
            () -> assertMenuCategoryResponse(response.getData().get(5), SUNDAE),
            () -> assertMenuCategoryResponse(response.getData().get(6), EOMUK),
            () -> assertMenuCategoryResponse(response.getData().get(7), TTEOKBOKKI),
            () -> assertMenuCategoryResponse(response.getData().get(8), TTANGKONGPPANG),
            () -> assertMenuCategoryResponse(response.getData().get(9), KKOCHI),
            () -> assertMenuCategoryResponse(response.getData().get(10), WAFFLE),
            () -> assertMenuCategoryResponse(response.getData().get(11), GUKWAPPANG),
            () -> assertMenuCategoryResponse(response.getData().get(12), TOAST),
            () -> assertMenuCategoryResponse(response.getData().get(13), GUNGOGUMA),
            () -> assertMenuCategoryResponse(response.getData().get(14), GUNOKSUSU)
        );
    }

    private void assertMenuCategoryResponse(MenuCategoryResponse response, MenuCategoryType type) {
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(type.getCategoryName()),
            () -> assertThat(response.getCategory()).isEqualTo(type),
            () -> assertThat(response.getDescription()).isEqualTo(type.getDescription())
        );
    }

}
