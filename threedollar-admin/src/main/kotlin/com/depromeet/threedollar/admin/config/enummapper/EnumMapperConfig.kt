package com.depromeet.threedollar.admin.config.enummapper

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.utils.EnumMapper
import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.user.domain.store.PaymentMethodType
import com.depromeet.threedollar.domain.user.domain.store.StoreType
import com.depromeet.threedollar.domain.user.domain.storedelete.DeleteReasonType
import com.depromeet.threedollar.domain.user.domain.user.UserSocialType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EnumMapperConfig {

    @Bean
    fun enumMapper(): EnumMapper {
        val enumMapper = EnumMapper()
        // common
        enumMapper.put("DayOfTheWeek", DayOfTheWeek::class.java)

        // user
        enumMapper.put("UserSocialType", UserSocialType::class.java)

        // store
        enumMapper.put("PaymentMethodType", PaymentMethodType::class.java)
        enumMapper.put("StoreType", StoreType::class.java)
        enumMapper.put("DeleteReasonType", DeleteReasonType::class.java)

        // faq
        enumMapper.put("FaqCategory", FaqCategory::class.java)

        // advertisement
        enumMapper.put("AdvertisementPositionType", AdvertisementPositionType::class.java)
        enumMapper.put("AdvertisementPlatformType", AdvertisementPlatformType::class.java)

        return enumMapper
    }

}
