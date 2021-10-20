package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.controller.AbstractControllerTest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_IMAGE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreImageControllerTest extends AbstractControllerTest {

    private StoreImageMockApiCaller storeImageMockApiCaller;

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
        storeImageMockApiCaller = new StoreImageMockApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AppearanceDayRepository appearanceDayRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        storeImageRepository.deleteAll();
    }

    @DisplayName("GET /api/v2/store/storeId/images")
    @Nested
    class 특정_가게에_등록된_이미지들을_조회한다 {

        @Test
        void 가게에_등록된_사진들을_조회한다() throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store.addMenus(Collections.singletonList(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            StoreImage storeImage1 = StoreImage.newInstance(store.getId(), testUser.getId(), "image1");
            StoreImage storeImage2 = StoreImage.newInstance(store.getId(), testUser.getId(), "image1");

            storeImageRepository.saveAll(List.of(storeImage1, storeImage2));

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.retrieveStoreImages(store.getId(), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertStoreImageResponse(response.getData().get(0), storeImage1.getId(), storeImage1.getUrl()),
                () -> assertStoreImageResponse(response.getData().get(1), storeImage2.getId(), storeImage2.getUrl()));
        }

        private void assertStoreImageResponse(StoreImageResponse response, Long storeImageId, String url) {
            assertThat(response.getImageId()).isEqualTo(storeImageId);
            assertThat(response.getUrl()).isEqualTo(url);
        }

    }

    @DisplayName("DELETE /api/v2/store/image")
    @Nested
    class 가게_이미지_삭제 {

        @Test
        void 성공시_200_OK() throws Exception {
            // given
            StoreImage storeImage = storeImageRepository.save(StoreImage.newInstance(100L, testUser.getId(), "https://store.com"));

            // when
            ApiResponse<String> response = storeImageMockApiCaller.deleteStoreImage(storeImage.getId(), token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

        @Test
        void 해당하는_이미지가_없으면_404_NOTFOUND() throws Exception {
            // when
            ApiResponse<String> response = storeImageMockApiCaller.deleteStoreImage(100L, token, 404);

            // then
            assertThat(response.getResultCode()).isEqualTo(NOT_FOUND_STORE_IMAGE_EXCEPTION.getCode());
            assertThat(response.getMessage()).isEqualTo(NOT_FOUND_STORE_IMAGE_EXCEPTION.getMessage());
            assertThat(response.getData()).isNull();
        }

    }

}
