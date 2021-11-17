package com.depromeet.threedollar.api.service.storeimage;

import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.service.storeimage.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageStatus;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StoreImageServiceTest extends SetupStoreServiceTest {

    private static final String IMAGE_URL = "https://image.url";

    private StoreImageService storeImageService;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        storeImageRepository.deleteAll();
    }

    @BeforeEach
    void setUpStoreImageService() {
        storeImageService = new StoreImageService(storeRepository, storeImageRepository, (request, file) -> IMAGE_URL);
    }

    @Nested
    class 가게_이미지_추가 {

        @Test
        void 가게에_새로운_이미지를_등록한다() {
            // given
            AddStoreImageRequest request = AddStoreImageRequest.testInstance(store.getId());

            // when
            storeImageService.addStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId);

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertThat(storeImageList).hasSize(1);
            assertStoreImage(storeImageList.get(0), store.getId(), userId, IMAGE_URL, StoreImageStatus.ACTIVE);
        }

        @Test
        void 존재하지_않는_가게에_이미지를_등록하면_NotFound_에러가_발생한다() {
            // given
            Long notFoundStoreId = -1L;
            AddStoreImageRequest request = AddStoreImageRequest.testInstance(notFoundStoreId);

            // when & then
            assertThatThrownBy(() -> storeImageService.addStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 가게_이미지_삭제 {

        @AutoSource
        @ParameterizedTest
        void 가게_이미지_삭제_성공시_해당_이미지가_INACTIVE로_변경된다(String imageUrl) {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, imageUrl);
            storeImageRepository.save(storeImage);

            // when
            storeImageService.deleteStoreImage(storeImage.getId());

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertThat(storeImageList).hasSize(1);
            assertStoreImage(storeImageList.get(0), store.getId(), userId, imageUrl, StoreImageStatus.INACTIVE);
        }

        @Test
        void 가게_이미지_삭제_요청시_해당하는_가게_이미지가_존재하지_않을경우_NOT_FOUND_STORE_EXCEPTION() {
            // given
            Long notFoundImageId = -1L;

            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(notFoundImageId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 가게_이미지_삭제_요청시_해당하는_가게_이미지가_INACTIVE_삭제일경우_NOT_FOUND_STORE_EXCEPTION() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, "https://profile.com");
            storeImage.delete();
            storeImageRepository.save(storeImage);

            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(storeImage.getId())).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 가게_이미지_조회 {

        @AutoSource
        @ParameterizedTest
        void 가게에_등록된_이미지_목록을_조회한다(String imageUrl) {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, imageUrl);
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.retrieveStoreImages(store.getId());

            // then
            assertThat(responses).hasSize(1);
            assertStoreImageResponse(responses.get(0), storeImage.getId(), storeImage.getUrl());
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회시_삭제된_이미지는_조회되지_않는다() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, "https://store-image.com");
            storeImage.delete();
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.retrieveStoreImages(store.getId());

            // then
            assertThat(responses).isEmpty();
        }

    }

    private void assertStoreImageResponse(StoreImageResponse storeImageResponse, Long id, String url) {
        assertThat(storeImageResponse.getImageId()).isEqualTo(id);
        assertThat(storeImageResponse.getUrl()).isEqualTo(url);
    }

    private void assertStoreImage(StoreImage storeImage, Long storeId, Long userId, String imageUrl, StoreImageStatus status) {
        assertThat(storeImage.getStoreId()).isEqualTo(storeId);
        assertThat(storeImage.getUserId()).isEqualTo(userId);
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
        assertThat(storeImage.getStatus()).isEqualTo(status);
    }

}
