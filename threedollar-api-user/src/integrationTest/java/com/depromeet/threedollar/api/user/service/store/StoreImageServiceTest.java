package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.api.user.service.store.support.StoreImageAssertions.assertStoreImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import com.depromeet.threedollar.api.core.provider.upload.UploadProvider;
import com.depromeet.threedollar.api.user.service.SetupStoreServiceTest;
import com.depromeet.threedollar.api.user.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.user.service.store.support.StoreImageAssertions;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageStatus;

@SpringBootTest
class StoreImageServiceTest extends SetupStoreServiceTest {

    private static final String IMAGE_URL = "https://image-storage.png";

    @Autowired
    private StoreImageService storeImageService;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @MockBean
    private UploadProvider uploadProvider;

    @AfterEach
    void cleanUp() {
        storeImageRepository.deleteAllInBatch();
        super.cleanup();
    }

    @Nested
    class UploadStoreImageTest {

        @Test
        void 가게_이미지_파일을_업로드하고_업로드된_외부_스토리지의_URL을_반환받는다() {
            // given
            when(uploadProvider.uploadFile(any())).thenReturn(IMAGE_URL);

            AddStoreImageRequest request = AddStoreImageRequest.testBuilder()
                .storeId(store.getId())
                .build();

            // when
            List<StoreImage> storeImages = storeImageService.uploadStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId);

            // then
            assertAll(
                () -> assertThat(storeImages).hasSize(1),
                () -> assertStoreImage(storeImages.get(0), storeId, userId, IMAGE_URL, StoreImageStatus.ACTIVE)
            );
        }

        @Test
        void 존재하지_않는_가게에_이미지를_업로드하면_NotFound_에러가_발생한다() {
            // given
            Long notFoundStoreId = -1L;

            AddStoreImageRequest request = AddStoreImageRequest.testBuilder()
                .storeId(notFoundStoreId)
                .build();

            // when & then
            assertThatThrownBy(() -> storeImageService.uploadStoreImages(request, List.of(new MockMultipartFile("name", new byte[]{})), userId)).isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class AddStoreImageTest {

        @Test
        void 가게에_새로운_이미지를_등록한다() {
            // given
            String storeImageUrl1 = "https://store-image1.png";
            String storeImageUrl2 = "https://store-image2.png";

            List<StoreImage> storeImages = List.of(
                StoreImageCreator.create(storeId, userId, storeImageUrl1),
                StoreImageCreator.create(storeId, userId, storeImageUrl2)
            );

            // when
            storeImageService.addStoreImages(storeImages);

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertThat(storeImageList).hasSize(2);
            assertStoreImage(storeImageList.get(0), store.getId(), userId, storeImageUrl1, StoreImageStatus.ACTIVE);
            assertStoreImage(storeImageList.get(1), store.getId(), userId, storeImageUrl2, StoreImageStatus.ACTIVE);
        }

    }

    @Nested
    class DeleteStoreImageTest {

        @Test
        void 가게_이미지_삭제_성공시_해당_이미지가_IN_ACTIVE_로_변경된다() {
            // given
            String imageUrl = "https://store-image.png";
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, imageUrl);
            storeImageRepository.save(storeImage);

            // when
            storeImageService.deleteStoreImage(storeImage.getId());

            // then
            List<StoreImage> storeImageList = storeImageRepository.findAll();
            assertAll(
                () -> assertThat(storeImageList).hasSize(1),
                () -> assertStoreImage(storeImageList.get(0), store.getId(), userId, imageUrl, StoreImageStatus.INACTIVE)
            );
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
    class GetStoreImagesTest {

        @Test
        void 가게에_등록된_이미지_목록을_조회한다() {
            // given
            String imageUrl = "https://store-good-image.png";
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, imageUrl);
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.getStoreImages(store.getId());

            // then
            assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> StoreImageAssertions.assertStoreImageResponse(responses.get(0), storeImage.getId(), imageUrl)
            );
        }

        @Test
        void 가게에_등록된_이미지_목록을_조회시_삭제된_이미지는_조회되지_않는다() {
            // given
            StoreImage storeImage = StoreImage.newInstance(store.getId(), userId, "https://store-image.com");
            storeImage.delete();
            storeImageRepository.save(storeImage);

            // when
            List<StoreImageResponse> responses = storeImageService.getStoreImages(store.getId());

            // then
            assertThat(responses).isEmpty();
        }

    }

}
