package com.depromeet.threedollar.api.userservice.config.enummodel;

import com.depromeet.threedollar.api.userservice.service.store.dto.type.UserStoreOrderType;
import com.depromeet.threedollar.common.type.BossStoreFeedbackType;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.common.utils.EnumMapper;
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumMapperConfig {

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        // common
        enumMapper.put("DayOfTheWeek", DayOfTheWeek.class);

        // user
        enumMapper.put("UserSocialType", UserSocialType.class);

        // store
        enumMapper.put("PaymentMethodType", PaymentMethodType.class);
        enumMapper.put("StoreType", StoreType.class);
        enumMapper.put("DeleteReasonType", DeleteReasonType.class);
        enumMapper.put("StoreOrderType", UserStoreOrderType.class);
        enumMapper.put("MenuCategoryType", UserMenuCategoryType.class);

        // faq
        enumMapper.put("FaqCategory", FaqCategory.class);

        // advertisement
        enumMapper.put("AdvertisementPositionType", AdvertisementPositionType.class);
        enumMapper.put("AdvertisementPlatformType", AdvertisementPlatformType.class);

        // visit
        enumMapper.put("VisitType", VisitType.class);

        // bossStore
        enumMapper.put("BossStoreOpenType", BossStoreOpenType.class);
        enumMapper.put("BossStoreFeedbackType", BossStoreFeedbackType.class);

        return enumMapper;
    }

}
