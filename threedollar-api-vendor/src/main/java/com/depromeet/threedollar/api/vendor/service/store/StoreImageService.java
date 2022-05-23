package com.depromeet.threedollar.api.vendor.service.store;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.depromeet.threedollar.api.core.provider.upload.UploadProvider;
import com.depromeet.threedollar.api.core.provider.upload.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.vendor.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.FileType;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreImageService {

    private static final FileType FILE_TYPE = FileType.STORE_IMAGE;
    private static final ApplicationType APPLICATION_TYPE = ApplicationType.USER_API;

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    private final UploadProvider uploadProvider;

    public List<StoreImage> uploadStoreImages(AddStoreImageRequest request, List<MultipartFile> imageFiles, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        if (imageFiles.size() == 1) {
            return Collections.singletonList(uploadStoreImage(request, imageFiles.get(0), userId));
        }
        List<CompletableFuture<StoreImage>> storeImageFutures = imageFiles.stream()
            .map(imageFile -> CompletableFuture.supplyAsync(() -> {
                String imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(imageFile, FILE_TYPE, APPLICATION_TYPE));
                return request.toEntity(request.getStoreId(), userId, imageUrl);
            }))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(storeImageFutures.toArray(new CompletableFuture[0]))
            .thenApply(storeImage -> storeImageFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
            ).join();
    }

    private StoreImage uploadStoreImage(AddStoreImageRequest request, MultipartFile imageFile, Long userId) {
        String imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(imageFile, FILE_TYPE, APPLICATION_TYPE));
        return request.toEntity(request.getStoreId(), userId, imageUrl);
    }

    @Transactional
    public List<StoreImageResponse> addStoreImages(List<StoreImage> storeImages) {
        return storeImageRepository.saveAll(storeImages).stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStoreImage(Long imageId) {
        StoreImage storeImage = StoreImageServiceUtils.findStoreImageById(storeImageRepository, imageId);
        storeImage.delete();
    }

    @Transactional(readOnly = true)
    public List<StoreImageResponse> getStoreImages(Long storeId) {
        StoreServiceUtils.validateExistsStore(storeRepository, storeId);
        return storeImageRepository.findAllByStoreId(storeId).stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
