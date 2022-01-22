package com.depromeet.threedollar.api.controller.storeimage;

import com.depromeet.threedollar.api.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.depromeet.threedollar.testhelper.assertion.StoreImageAssertionHelper.assertStoreImageResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreImageControllerTest extends SetupStoreControllerTest {

    private StoreImageMockApiCaller storeImageMockApiCaller;

    @BeforeEach
    void setUp() {
        storeImageMockApiCaller = new StoreImageMockApiCaller(mockMvc, objectMapper);
    }

    @Autowired
    private StoreImageRepository storeImageRepository;

    @AfterEach
    void cleanUp() {
        storeImageRepository.deleteAllInBatch();
        super.cleanup();
    }

    @DisplayName("GET /api/v2/store/storeId/images")
    @Nested
    class 특정_가게에_등록된_이미지들을_조회한다 {

        @Test
        void 가게에_등록된_사진들을_조회한다() throws Exception {
            // given
            String imageUrl1 = "imageUrl1";
            String imageUrl2 = "imageUrl2";

            StoreImage storeImage1 = StoreImage.newInstance(store, testUser.getId(), imageUrl1);
            StoreImage storeImage2 = StoreImage.newInstance(store, testUser.getId(), imageUrl2);
            storeImageRepository.saveAll(List.of(storeImage1, storeImage2));

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.getStoreImages(storeId, 200);

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

        @Test
        void 가게_이미지_삭제요청_성공시_200_OK() throws Exception {
            // given
            StoreImage storeImage = storeImageRepository.save(StoreImage.newInstance(store, testUser.getId(), "imageUrl"));

            // when
            ApiResponse<String> response = storeImageMockApiCaller.deleteStoreImage(storeImage.getId(), token, 200);

            // then
            assertThat(response.getResultCode()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
            assertThat(response.getData()).isEqualTo(ApiResponse.SUCCESS.getData());
        }

    }

}
