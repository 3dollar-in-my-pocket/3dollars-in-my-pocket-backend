package com.depromeet.threedollar.api.userservice.service.store;

import com.depromeet.threedollar.api.userservice.SetupStoreIntegrationTest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.infrastructure.s3.infra.FileStorageClient;
import com.depromeet.threedollar.infrastructure.s3.provider.UploadProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.depromeet.threedollar.api.userservice.service.store.support.StoreImageAssertions.assertStoreImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreImageServiceTest extends SetupStoreIntegrationTest {

    private static final String IMAGE_URL = "https://image-storage.png";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreImageRepository storeImageRepository;

    private StoreImageService storeImageService;

    @BeforeEach
    void setUp() {
        storeImageService = new StoreImageService(storeRepository, storeImageRepository, new UploadProvider(new StubFileStorageClient()));
    }

    @Nested
    class UploadStoreImageTest {

        @Test
        void ??????_?????????_?????????_???????????????_????????????_??????_???????????????_URL???_???????????????() {
            // given
            AddStoreImageRequest request = AddStoreImageRequest.testBuilder()
                .storeId(store.getId())
                .build();

            // when
            List<StoreImage> storeImages = storeImageService.uploadStoreImages(request, List.of(new MockMultipartFile("name.jpeg", "originName.jpeg", "image/jpeg", new byte[]{})), userId);

            // then
            assertAll(
                () -> assertThat(storeImages).hasSize(1),
                () -> assertStoreImage(storeImages.get(0), storeId, userId, IMAGE_URL, StoreImageStatus.ACTIVE)
            );
        }

        @Test
        void ????????????_??????_?????????_????????????_???????????????_NotFound_?????????_????????????() {
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
        void ?????????_?????????_???????????????_????????????() {
            // given
            String storeImageUrl1 = "https://store-image1.png";
            String storeImageUrl2 = "https://store-image2.png";

            List<StoreImage> storeImages = List.of(
                StoreImageFixture.create(storeId, userId, storeImageUrl1),
                StoreImageFixture.create(storeId, userId, storeImageUrl2)
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
        void ??????_?????????_?????????_??????_????????????_IN_ACTIVE_???_????????????() {
            // given
            String imageUrl = "https://store-image.png";
            StoreImage storeImage = StoreImageFixture.create(storeId, userId, imageUrl);
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
        void ??????_?????????_??????_?????????_????????????_??????_????????????_????????????_????????????_NOT_FOUND_STORE_EXCEPTION() {
            // given
            Long notFoundImageId = -1L;

            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(notFoundImageId)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void ??????_?????????_??????_?????????_????????????_??????_????????????_??????_???????????????_NOT_FOUND_STORE_EXCEPTION() {
            // given
            StoreImage storeImage = StoreImageFixture.createDeleted(storeId);
            storeImageRepository.save(storeImage);

            // when & then
            assertThatThrownBy(() -> storeImageService.deleteStoreImage(storeImage.getId())).isInstanceOf(NotFoundException.class);
        }

    }

    private static class StubFileStorageClient implements FileStorageClient {

        @Override
        public void uploadFile(@NotNull MultipartFile file, @NotNull String fileName) {
        }

        @Override
        public String getFileUrl(@NotNull String fileName) {
            return IMAGE_URL;
        }
    }

}
