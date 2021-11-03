package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.store.*;
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
    class 가게_이미지_등록 {

        @Test
        void 가게_이미지_등록_성공시_이미지_정보가_추가된다() {
            // given
            AddStoreImageRequest request = AddStoreImageRequest.testInstance(store.getId());

            // when
            storeImageService.addStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId);

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertThat(storeImageList).hasSize(1);
            assertStoreImage(storeImageList.get(0), store.getId(), userId, IMAGE_URL, StoreImageStatus.ACTIVE);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_이미지_등록_요청시_해당하는_가게가_없는경우_NOT_FOUND_STORE_EXCEPTION(Long notFoundStoreId) {
            // given
            AddStoreImageRequest request = AddStoreImageRequest.testInstance(notFoundStoreId);

            // when & then
            assertThatThrownBy(() -> storeImageService.addStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 가게_이미지_삭제 {

        @Test
        void 가게_이미지_삭제_성공시_해당_이미지가_INACTIVE로_변경된다() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, IMAGE_URL);
            storeImageRepository.save(storeImage);

            // when
            storeImageService.deleteStoreImage(storeImage.getId());

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertThat(storeImageList).hasSize(1);
            assertStoreImage(storeImageList.get(0), store.getId(), userId, IMAGE_URL, StoreImageStatus.INACTIVE);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_이미지_삭제_요청시_해당하는_가게_이미지가_존재하지_않을경우_NOT_FOUND_STORE_EXCEPTION(Long notFoundImageId) {
            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(notFoundImageId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 가게_이미지_삭제_요청시_해당하는_가게_이미지가_INACTIVE_삭제일경우_NOT_FOUND_STORE_EXCEPTION() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, IMAGE_URL);
            storeImage.delete();
            storeImageRepository.save(storeImage);

            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(storeImage.getId())).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class 가게_이미지_조회 {

        @Test
        void 가게_이미지_조회_성공시_해당_이미지_정보가_반환된다() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, IMAGE_URL);
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.retrieveStoreImages(store.getId());

            // then
            assertThat(responses).hasSize(1);
            asserStoreImageResponse(responses.get(0), storeImage.getId(), storeImage.getUrl());
        }

        @Test
        void  가게_이미지_조회시_삭제된_이미지는_조회되지_않는다() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, IMAGE_URL);
            storeImage.delete();
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.retrieveStoreImages(store.getId());

            // then
            assertThat(responses).isEmpty();
        }

    }

    private void asserStoreImageResponse(StoreImageResponse storeImageResponse, Long id, String url) {
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
