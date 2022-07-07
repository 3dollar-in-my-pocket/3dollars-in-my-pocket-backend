package com.depromeet.threedollar.api.bossservice.config.enummodel

import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.utils.EnumMapper
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
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
        enumMapper.put("BossAccountSocialType", com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType::class.java)

        // bossStore
        enumMapper.put("BossStoreOpenType", BossStoreOpenType::class.java)
        enumMapper.put("BossStoreFeedbackType", BossStoreFeedbackType::class.java)

        return enumMapper
    }

}

