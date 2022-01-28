package com.depromeet.threedollar.boss.api.config.enummodel

import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.common.type.FileType
import com.depromeet.threedollar.common.utils.EnumMapper
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import com.depromeet.threedollar.document.boss.document.feedback.BossStoreFeedbackType
import com.depromeet.threedollar.document.boss.document.store.BossStoreOpenType
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
        enumMapper.put("PushSeetingStatus", PushSettingsStatus::class.java)

        // bossStore
        enumMapper.put("BossStoreOpenType", BossStoreOpenType::class.java)
        enumMapper.put("BossStoreFeedbackType", BossStoreFeedbackType::class.java)

        return enumMapper
    }

}

