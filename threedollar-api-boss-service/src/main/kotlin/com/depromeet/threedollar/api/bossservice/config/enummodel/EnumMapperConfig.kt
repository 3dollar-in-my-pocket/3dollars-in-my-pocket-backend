package com.depromeet.threedollar.api.bossservice.config.enummodel

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.type.PushOptionsType
import com.depromeet.threedollar.common.utils.EnumMapper
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.PushPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.infrastructure.s3.common.type.FileType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EnumMapperConfig {

    @Bean
    fun enumMapper(): EnumMapper {
        val enumMapper = EnumMapper()
        // common
        enumMapper.put("DayOfTheWeek", DayOfTheWeek::class.java)
        enumMapper.put("FileType", FileType::class.java)

        // bossAccount
        enumMapper.put("BossAccountSocialType", BossAccountSocialType::class.java)

        // bossStore
        enumMapper.put("BossStoreOpenType", BossStoreOpenType::class.java)
        enumMapper.put("BossStoreFeedbackType", BossStoreFeedbackType::class.java)

        // boss registration
        enumMapper.put("BoosRegistrationRejectReasonType", BossRegistrationRejectReasonType::class.java)

        // push
        enumMapper.put("PushPlatformType", PushPlatformType::class.java)
        enumMapper.put("PushOptions", PushOptionsType::class.java)

        // faq
        enumMapper.put("FaqCategory", FaqCategory::class.java)

        // advertisement
        enumMapper.put("AdvertisementPositionType", AdvertisementPositionType::class.java)
        enumMapper.put("AdvertisementPlatformType", AdvertisementPlatformType::class.java)

        return enumMapper
    }

}

