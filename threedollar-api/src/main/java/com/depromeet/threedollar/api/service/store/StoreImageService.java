package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.upload.UploadService;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.service.upload.dto.request.ImageUploadRequest;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import com.depromeet.threedollar.domain.domain.store.StoreImage;
import com.depromeet.threedollar.domain.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_IMAGE_EXCEPTION;

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
        StoreImage storeImage = findStoreImageById(imageId);
        storeImage.delete();
        storeImageRepository.save(storeImage);
    }

    private StoreImage findStoreImageById(Long storeImageId) {
        StoreImage storeImage = storeImageRepository.findStoreImageById(storeImageId);
        if (storeImage == null) {
            throw new NotFoundException(String.format("해당하는 가게 이미지 (%s)는 존재하지 않습니다", storeImageId), NOT_FOUND_STORE_IMAGE_EXCEPTION);
        }
        return storeImage;
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
