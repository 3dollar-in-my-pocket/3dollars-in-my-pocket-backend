package com.depromeet.threedollar.domain.user.domain.storeimage;

import com.depromeet.threedollar.domain.user.domain.storeimage.repository.StoreImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositoryCustom {

}
