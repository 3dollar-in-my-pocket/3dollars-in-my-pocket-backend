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
            Faq faq1 = FaqCreator.builder()
                .question("게시글을 수정하고 싶어요")
                .answer("가게 정보 수정은 상세 페이지의 '정보 수정'을 통해 가능합니다.")
                .category(FaqCategory.BOARD)
                .build();
            Faq faq2 = FaqCreator.builder()
                .question("가슴속 삼천원 서비스팀과 협업하고 싶어요.")
                .answer("각종 광고 및 인터뷰, 협업 문의는 저희 가슴속 3천원 대표 메일을 통해 연락 주시면 빠르게 회신 가능합니다. 많은 연락과 관심 부탁드려요!\n3dollarinmypocket@gmail.com")
                .category(FaqCategory.ETC)
                .build();
            faqRepository.saveAll(List.of(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveFaqsByCategory(200);

            // then
            assertThat(response.getData()).hasSize(2);
            assertFaqResponse(response.getData().get(0), faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), faq1.getCategory());
            assertFaqResponse(response.getData().get(1), faq2.getId(), faq2.getQuestion(), faq2.getAnswer(), faq2.getCategory());
        }

    }

    @DisplayName("GET /api/v2/faqs?category=BOARD")
    @Nested
    class GetFaqsApiTest {

        @Test
        void 특정_카테고리의_FAQ_리스트를_조회한다() throws Exception {
            // given
            Faq faq1 = FaqCreator.builder()
                .question("게시글을 삭제하고 싶어요.")
                .answer("가게 정보 삭제는 상세페이지 우측 상단의 '삭제요청'을 통해 가능합니다. 가게 삭제는 3명 이상의 사용자에게 요청이 들어오면 완료됩니다.\n잘못된 가게 정보를 빠르게 삭제하고 싶다면 '문의하기'를 통해 메일을 보내주세요!")
                .category(FaqCategory.BOARD)
                .build();
            Faq faq2 = FaqCreator.builder()
                .question("리뷰 수정이나 삭제를 하고 싶어요.")
                .answer("리뷰 우측 상단의 메뉴 아이콘을 눌러 '리뷰 수정' 또는 '리뷰 삭제'를 선택해주세요.\n자신이 작성한 리뷰가 아닐 경우 수정이나 삭제가 불가능합니다.")
                .category(FaqCategory.REVIEW_MENU)
                .build();
            faqRepository.saveAll(List.of(faq1, faq2));

            // when
            ApiResponse<List<FaqResponse>> response = faqMockApiCaller.retrieveFaqsByCategory(FaqCategory.BOARD, 200);

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
