package com.depromeet.threedollar.domain.domain.storeimage.repository;

import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;

import java.util.List;

public interface StoreImageRepositoryCustom {

    StoreImage findStoreImageById(Long storeImageId);

    List<StoreImage> findAllByStoreId(Long storeId);

}
