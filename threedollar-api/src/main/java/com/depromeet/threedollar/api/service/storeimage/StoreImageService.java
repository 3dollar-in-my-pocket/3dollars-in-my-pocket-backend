package com.depromeet.threedollar.api.service.storeimage;

import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.storeimage.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.service.upload.UploadService;
import com.depromeet.threedollar.api.service.upload.dto.request.ImageUploadRequest;
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
    private final UploadService uploadService;

    public List<StoreImageResponse> addStoreImages(AddStoreImageRequest request, List<MultipartFile> images, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        List<StoreImage> storeImages = storeImageRepository.saveAll(images.stream()
            .map(image -> addStoreImage(request, image, userId))
            .collect(Collectors.toList()));
        return storeImages.stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

    private StoreImage addStoreImage(AddStoreImageRequest request, MultipartFile image, Long userId) {
        String imageUrl = uploadService.uploadFile(ImageUploadRequest.of(ImageType.STORE), image);
        return StoreImage.newInstance(request.getStoreId(), userId, imageUrl);
    }

    @Transactional
    public void deleteStoreImage(Long imageId) {
        StoreImage storeImage = StoreImageServiceUtils.findStoreImageById(storeImageRepository, imageId);
        storeImage.delete();
        storeImageRepository.save(storeImage);
    }

    @Transactional(readOnly = true)
    public List<StoreImageResponse> retrieveStoreImages(Long storeId) {
        StoreServiceUtils.validateExistsStore(storeRepository, storeId);
        List<StoreImage> storeImages = storeImageRepository.findAllByStoreId(storeId);
        return storeImages.stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
