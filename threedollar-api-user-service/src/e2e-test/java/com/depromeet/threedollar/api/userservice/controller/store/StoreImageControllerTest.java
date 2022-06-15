package com.depromeet.threedollar.api.userservice.controller.store;

import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreImageAssertions.assertStoreImageResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.SetupStoreControllerTest;
import com.depromeet.threedollar.api.userservice.controller.store.support.StoreImageAssertions;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageCreator;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageStatus;

class StoreImageControllerTest extends SetupStoreControllerTest {

    private StoreImageMockApiCaller storeImageMockApiCaller;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @BeforeEach
    void setUp() {
        storeImageMockApiCaller = new StoreImageMockApiCaller(mockMvc, objectMapper);
    }

    @DisplayName("GET /api/v2/store/storeId/images")
    @Nested
    class GetStoreImagesApiTest {

        @Test
        void 가게에_등록된_가게_이미지_목록을_조회합니다() throws Exception {
            // given
            String imageUrl1 = "https://image1.png";
            String imageUrl2 = "https://image2.png";

            StoreImage storeImage1 = StoreImageCreator.create(storeId, user.getId(), imageUrl1);
            StoreImage storeImage2 = StoreImageCreator.create(storeId, user.getId(), imageUrl2);
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

        @Test
        void 가게에_등록된_이미지_목록을_조회한다() throws Exception {
            // given
            String imageUrl = "https://store-good-image.png";
            StoreImage storeImage = StoreImageCreator.create(store.getId(), user.getId(), imageUrl);
            storeImageRepository.save(storeImage);

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.getStoreImages(storeId, token, 200);

            // then
            assertAll(
                () -> assertThat(response.getData()).hasSize(1),
                () -> StoreImageAssertions.assertStoreImageResponse(response.getData().get(0), storeImage.getId(), imageUrl)
            );
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회시_삭제된_이미지는_조회되지_않는다() throws Exception {
            // given
            StoreImage storeImage = StoreImageCreator.create(store.getId(), user.getId(), "https://deleted-store-image.png", StoreImageStatus.INACTIVE);
            storeImageRepository.save(storeImage);

            // when
            ApiResponse<List<StoreImageResponse>> response = storeImageMockApiCaller.getStoreImages(storeId, token, 200);

            // then
            assertThat(response.getData()).isEmpty();
        }

    }

    @DisplayName("DELETE /api/v2/store/image")
    @Nested
    class DeleteStoreImageApiTest {

        @Test
        void 가게의_특정_이미지를_삭제합니다() throws Exception {
            // given
            StoreImage storeImage = storeImageRepository.save(StoreImageCreator.create(storeId, user.getId(), "https://image-url.png"));

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
