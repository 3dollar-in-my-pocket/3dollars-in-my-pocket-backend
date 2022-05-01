package com.depromeet.threedollar.api.user.controller.faq;

import static com.depromeet.threedollar.api.user.controller.faq.support.FaqAssertions.assertFaqResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.core.mapper.user.faq.dto.response.FaqCategoryResponse;
import com.depromeet.threedollar.api.core.service.user.faq.dto.response.FaqResponse;
import com.depromeet.threedollar.api.user.controller.SetupUserControllerTest;
import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCreator;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqRepository;

class FaqControllerTest extends SetupUserControllerTest {

    private FaqMockApiCaller faqMockApiCaller;

    @Autowired
    private FaqRepository faqRepository;

    @BeforeEach
    void setUp() {
        faqMockApiCaller = new FaqMockApiCaller(mockMvc, objectMapper);
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
        faqRepository.deleteAllInBatch();
    }

    @DisplayName("GET /api/v2/faqs")
    @Nested
    class AddFaqApiTest {

        @Test
        void FAQ_전체_리스트를_조회한다() throws Exception {
            // given
            Faq faq1 = FaqCreator.create("앱 이름이 뭔가요?", "가슴속 3천원입니다", FaqCategory.CATEGORY);
            Faq faq2 = FaqCreator.create("가게 등록이 안되요", "가게 등록은 어찌어찌 할 수 있습니다", FaqCategory.BOARD);
            faqRepository.saveAll(List.of(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveFaqsByCategory(200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertFaqResponse(response.getData().get(0), faq2.getId(), faq2.getQuestion(), faq2.getAnswer(), faq2.getCategory());
            assertFaqResponse(response.getData().get(1), faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), faq1.getCategory());
        }

    }

    @DisplayName("GET /api/v2/faqs?category=BOARD")
    @Nested
    class GetFaqsApiTest {

        @Test
        void 특정_카테고리의_FAQ_리스트를_조회한다() throws Exception {
            // given
            Faq faq1 = FaqCreator.create("가게 삭제를 하려면 어떻게 하나요?", "가게 삭제를 하려면 어찌저찌 해야합니다", FaqCategory.CATEGORY);
            Faq faq2 = FaqCreator.create("리뷰 등록은 어떻게 하나요?", "리뷰 등록은 어찌저찌 하면 됩니다", FaqCategory.BOARD);
            faqRepository.saveAll(List.of(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveFaqsByCategory(FaqCategory.CATEGORY, 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertFaqResponse(response.getData().get(0), faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), faq1.getCategory());
        }

    }

    @DisplayName("GET /api/v2/faq/categories")
    @Nested
    class GetFaqCategoriesApiTest {

        @Test
        void FAQ_카테고리_리스트를_조회한다K() throws Exception {
            // when
            ApiResponse<List<FaqCategoryResponse>> response = faqMockApiCaller.retrieveFaqCategories(200);

            // then
            assertThat(response.getData()).hasSize(FaqCategory.values().length);
        }

    }

}
