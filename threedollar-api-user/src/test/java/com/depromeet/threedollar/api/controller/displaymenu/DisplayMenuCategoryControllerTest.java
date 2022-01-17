package com.depromeet.threedollar.api.controller.displaymenu;

import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.application.service.displaymenu.dto.response.DisplayMenuCategoryResponse;
import com.depromeet.threedollar.domain.user.domain.menucategory.DisplayMenuCategory;
import com.depromeet.threedollar.domain.user.domain.menucategory.DisplayMenuCategoryRepository;
import com.depromeet.threedollar.domain.user.domain.menucategory.DisplayMenuCategoryStatusType;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class DisplayMenuCategoryControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private DisplayMenuCategoryRepository displayMenuCategoryRepository;

    @DisplayName("GET /api/v2/store/menu/categories")
    @Test
    void 활성화된_메뉴_카테고리의_목록들을_displayOrder가_낮은것부터_조회한다() throws Exception {
        // given
        DisplayMenuCategory displayMenuCategory1 = DisplayMenuCategory.testBuilder()
            .categoryType(MenuCategoryType.DALGONA)
            .name("달고나")
            .description("456번째 달고나")
            .iconUrl("https://icon1.url")
            .isNew(true)
            .status(DisplayMenuCategoryStatusType.ACTIVE)
            .displayOrder(1)
            .build();

        DisplayMenuCategory displayMenuCategory2 = DisplayMenuCategory.testBuilder()
            .categoryType(MenuCategoryType.BUNGEOPPANG)
            .name("붕어빵")
            .description("붕어빵 만나기 30초 전")
            .iconUrl("https://icon2.url")
            .isNew(false)
            .status(DisplayMenuCategoryStatusType.ACTIVE)
            .displayOrder(2)
            .build();

        DisplayMenuCategory inactivatedMenuCategory = DisplayMenuCategory.testBuilder()
            .categoryType(MenuCategoryType.GUNGOGUMA)
            .name("군고구마")
            .description("군구구마")
            .iconUrl("https://icon3.url")
            .isNew(true)
            .status(DisplayMenuCategoryStatusType.INACTIVE)
            .displayOrder(3)
            .build();

        displayMenuCategoryRepository.saveAll(List.of(displayMenuCategory1, displayMenuCategory2, inactivatedMenuCategory));

        // when
        ApiResponse<List<DisplayMenuCategoryResponse>> response = objectMapper.readValue(
            mockMvc.perform(get("/api/v2/store/menu/categories"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );

        // then
        assertAll(
            () -> assertThat(response.getData()).hasSize(2),
            () -> assertDisplayMenuCategoryResponse(response.getData().get(0), displayMenuCategory1),
            () -> assertDisplayMenuCategoryResponse(response.getData().get(1), displayMenuCategory2)
        );
    }

    private void assertDisplayMenuCategoryResponse(DisplayMenuCategoryResponse response, DisplayMenuCategory displayMenuCategory) {
        assertAll(
            () -> assertThat(response.getCategory()).isEqualTo(displayMenuCategory.getCategoryType()),
            () -> assertThat(response.getName()).isEqualTo(displayMenuCategory.getName()),
            () -> assertThat(response.getDescription()).isEqualTo(displayMenuCategory.getDescription()),
            () -> assertThat(response.getIconUrl()).isEqualTo(displayMenuCategory.getIconUrl())
        );
    }

}
