package com.depromeet.threedollar.api.user.service.store;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.depromeet.threedollar.api.core.provider.upload.UploadProvider;
import com.depromeet.threedollar.api.core.provider.upload.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.FileType;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    private final UploadProvider uploadProvider;

    public List<StoreImage> uploadStoreImages(AddStoreImageRequest request, List<MultipartFile> images, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        return images.stream()
            .map(imageFile -> uploadProvider.uploadFile(ImageUploadFileRequest.of(imageFile, FileType.STORE_IMAGE, ApplicationType.USER_API)))
            .map(imageUrl -> request.toEntity(request.getStoreId(), userId, imageUrl))
            .collect(Collectors.toList());
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
