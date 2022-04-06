package com.depromeet.threedollar.api.user.controller.store;

import com.depromeet.threedollar.api.user.controller.SetupStoreControllerTest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.depromeet.threedollar.api.user.testhelper.assertions.StoreImageAssertionHelper.assertStoreImageResponse;
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
    class GetStoreImagesApiTest {

        @Test
        void 가게에_등록된_사진들을_조회한다() throws Exception {
            // given
            String imageUrl1 = "imageUrl1";
            String imageUrl2 = "imageUrl2";

            StoreImage storeImage1 = StoreImage.newInstance(store, user.getId(), imageUrl1);
            StoreImage storeImage2 = StoreImage.newInstance(store, user.getId(), imageUrl2);
            storeImageRepository.saveAll(List.of(storeImage1, storeImage2));

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.getStoreImages(storeId, token, 200);

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
    class DeleteStoreImageApiTest {

        @Test
        void 가게_이미지_삭제요청_성공시_200_OK() throws Exception {
            // given
            StoreImage storeImage = storeImageRepository.save(StoreImage.newInstance(store, user.getId(), "imageUrl"));

            // when
            ApiResponse<String> response = storeImageMockApiCaller.deleteStoreImage(storeImage.getId(), token, 200);

            // then
            assertAll(
                () -> assertThat(response.getResultCode()).isEmpty(),
                () -> assertThat(response.getMessage()).isEmpty(),
                () -> assertThat(response.getData()).isEqualTo(ApiResponse.OK.getData())
            );
        }

    }

}
