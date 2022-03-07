package com.depromeet.threedollar.api.user.config.enummodel;

import com.depromeet.threedollar.api.user.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.utils.EnumMapper;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreType;
import com.depromeet.threedollar.domain.rds.user.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
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
        enumMapper.put("StoreOrderType", StoreOrderType.class);
        enumMapper.put("MenuCategoryType", MenuCategoryType.class);

        // faq
        enumMapper.put("FaqCategory", FaqCategory.class);

        // advertisement
        enumMapper.put("AdvertisementPositionType", AdvertisementPositionType.class);
        enumMapper.put("AdvertisementPlatformType", AdvertisementPlatformType.class);

        // visit
        enumMapper.put("VisitType", VisitType.class);
        return enumMapper;
    }

}
