package com.depromeet.threedollar.api.controller.storeimage;

import com.depromeet.threedollar.api.controller.SetupUserControllerTest;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.depromeet.threedollar.api.assertutils.assertStoreImageUtils.assertStoreImageResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreImageControllerTest extends SetupUserControllerTest {

    private StoreImageMockApiCaller storeImageMockApiCaller;

    @BeforeEach
    void setUp() {
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

        @AutoSource
        @ParameterizedTest
        void 가게에_등록된_사진들을_조회한다(String imageUrl1, String imageUrl2) throws Exception {
            // given
            Store store = StoreCreator.create(testUser.getId(), "storeName", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            StoreImage storeImage1 = StoreImage.newInstance(store.getId(), testUser.getId(), imageUrl1);
            StoreImage storeImage2 = StoreImage.newInstance(store.getId(), testUser.getId(), imageUrl2);

            storeImageRepository.saveAll(List.of(storeImage1, storeImage2));

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.retrieveStoreImages(store.getId(), 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(2),
                () -> assertStoreImageResponse(response.getData().get(0), storeImage1.getId(), imageUrl1),
                () -> assertStoreImageResponse(response.getData().get(1), storeImage2.getId(), imageUrl2)
            );
        }

    }

    @DisplayName("DELETE /api/v2/store/image")
    @Nested
    class 가게_이미지_삭제 {

        @AutoSource
        @ParameterizedTest
        void 가게_이미지_삭제요청_성공시_200_OK(Long storeId, String imageUrl) throws Exception {
            // given
            StoreImage storeImage = storeImageRepository.save(StoreImage.newInstance(storeId, testUser.getId(), imageUrl));

            // when
            ApiResponse<String> response = storeImageMockApiCaller.deleteStoreImage(storeImage.getId(), token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

    }

}
