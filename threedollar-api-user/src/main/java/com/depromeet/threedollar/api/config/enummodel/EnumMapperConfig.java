package com.depromeet.threedollar.api.config.enummodel;

import com.depromeet.threedollar.api.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.utils.EnumMapper;
import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType;
import com.depromeet.threedollar.domain.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.user.domain.store.StoreType;
import com.depromeet.threedollar.domain.user.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.user.domain.visit.VisitType;
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

        // faq
        enumMapper.put("FaqCategory", FaqCategory.class);

        // popup
        enumMapper.put("PopupPositionType", PopupPositionType.class);

        // visit
        enumMapper.put("VisitType", VisitType.class);
        return enumMapper;
    }

}
