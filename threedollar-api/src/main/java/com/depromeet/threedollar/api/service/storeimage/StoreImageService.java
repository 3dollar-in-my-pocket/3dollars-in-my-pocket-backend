package com.depromeet.threedollar.api.service.storeimage;

import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.storeimage.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.provider.upload.UploadProvider;
import com.depromeet.threedollar.api.provider.upload.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    private final UploadProvider uploadProvider;

    public List<StoreImageResponse> addStoreImages(AddStoreImageRequest request, List<MultipartFile> imageFiles, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        List<StoreImage> storeImages = imageFiles.stream()
            .map(this::uploadImage)
            .map(imageUrl -> request.toEntity(userId, imageUrl))
            .collect(Collectors.toList());

        return storeImageRepository.saveAll(storeImages).stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

    private String uploadImage(MultipartFile imageFile) {
        return uploadProvider.uploadFile(ImageUploadFileRequest.of(ImageType.STORE), imageFile);
    }

    @Transactional
    public void deleteStoreImage(Long imageId) {
        StoreImage storeImage = StoreImageServiceUtils.findStoreImageById(storeImageRepository, imageId);
        storeImage.delete();
        storeImageRepository.save(storeImage);
    }

    @Transactional(readOnly = true)
    public List<StoreImageResponse> getStoreImages(Long storeId) {
        StoreServiceUtils.validateExistsStore(storeRepository, storeId);
        return storeImageRepository.findAllByStoreId(storeId).stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
