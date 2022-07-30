package com.depromeet.threedollar.api.core.service.service.commonservice.advertisement.dto.request

import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import javax.validation.constraints.NotNull

data class RetrieveAdvertisementsRequest(
    @field:NotNull(message = "{advertisement.platform.notnull}")
    val platform: AdvertisementPlatformType?,

    @field:NotNull(message = "{advertisement.position.notnull}")
    val position: AdvertisementPositionType = AdvertisementPositionType.SPLASH,
)
