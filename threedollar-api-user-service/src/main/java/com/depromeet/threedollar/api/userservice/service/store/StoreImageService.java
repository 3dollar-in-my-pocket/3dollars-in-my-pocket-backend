package com.depromeet.threedollar.api.userservice.service.store;

import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.infrastructure.s3.common.type.FileType;
import com.depromeet.threedollar.infrastructure.s3.provider.UploadProvider;
import com.depromeet.threedollar.infrastructure.s3.provider.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.infrastructure.s3.provider.dto.request.UploadFileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    private final UploadProvider uploadProvider;

    public List<StoreImage> uploadStoreImages(AddStoreImageRequest request, List<MultipartFile> imageFiles, Long userId) {
        StoreServiceHelper.validateExistsStore(storeRepository, request.getStoreId());

        List<UploadFileRequest> uploadFileRequests = imageFiles.stream()
            .map(imageFile -> ImageUploadFileRequest.of(imageFile, FileType.STORE_IMAGE, ApplicationType.USER_API))
            .collect(Collectors.toList());

        return uploadProvider.uploadFiles(uploadFileRequests).stream()
            .map(uploadResponse -> request.toEntity(userId, uploadResponse.getFileUrl()))
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
        StoreImage storeImage = StoreImageServiceHelper.findStoreImageById(storeImageRepository, imageId);
        storeImage.delete();
        storeImageRepository.save(storeImage);
    }

}
