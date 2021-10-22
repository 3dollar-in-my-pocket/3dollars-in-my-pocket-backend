package com.depromeet.threedollar.api.controller.faq;

import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.application.mapper.faq.dto.response.FaqCategoryResponse;
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse;
import com.depromeet.threedollar.domain.domain.faq.Faq;
import com.depromeet.threedollar.domain.domain.faq.FaqCategory;
import com.depromeet.threedollar.domain.domain.faq.FaqCreator;
import com.depromeet.threedollar.domain.domain.faq.FaqRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FaqControllerTest extends AbstractControllerTest {

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
        faqRepository.deleteAll();
    }

    @DisplayName("GET /api/v2/faqs")
    @Nested
    class FAQ_전체_조회 {

        @Test
        void FAQ_전체_리스트를_조회한다() throws Exception {
            // given
            Faq faq1 = FaqCreator.create("question1", "answer1", FaqCategory.CATEGORY);
            Faq faq2 = FaqCreator.create("question2", "answer2", FaqCategory.BOARD);
            faqRepository.saveAll(Arrays.asList(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveAllFaqs(200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertFaqResponse(response.getData().get(0), faq2.getId(), faq2.getQuestion(), faq2.getAnswer(), faq2.getCategory());
            assertFaqResponse(response.getData().get(1), faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), faq1.getCategory());
        }

    }

    @DisplayName("GET /api/v2/faqs?category=BOARD")
    @Nested
    class FAQ_특정_카테고리_조회 {

        @Test
        void 특정_카테고리의_FAQ_리스트를_조회한다() throws Exception {
            // given
            Faq faq1 = FaqCreator.create("question1", "answer1", FaqCategory.CATEGORY);
            Faq faq2 = FaqCreator.create("question2", "answer2", FaqCategory.BOARD);
            faqRepository.saveAll(Arrays.asList(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveAllFaqs(FaqCategory.CATEGORY, 200);

            // then
            assertThat(response.getData()).hasSize(1);
            assertFaqResponse(response.getData().get(0), faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), faq1.getCategory());
        }

    }

    @DisplayName("GET /api/v2/faq/categories")
    @Nested
    class FAQ_카테고리_리스트_조회 {

        @Test
        void FAQ_카테고리_리스트를_조회한다K() throws Exception {
            // when
            ApiResponse<List<FaqCategoryResponse>> response = faqMockApiCaller.retrieveAllFaqCategories(200);

            // then
            assertThat(response.getData()).hasSize(FaqCategory.values().length);
        }

    }

    private void assertFaqResponse(FaqResponse faqResponse, Long id, String question, String answer, FaqCategory category) {
        assertThat(faqResponse.getFaqId()).isEqualTo(id);
        assertThat(faqResponse.getQuestion()).isEqualTo(question);
        assertThat(faqResponse.getAnswer()).isEqualTo(answer);
        assertThat(faqResponse.getCategory()).isEqualTo(category);
    }

}
