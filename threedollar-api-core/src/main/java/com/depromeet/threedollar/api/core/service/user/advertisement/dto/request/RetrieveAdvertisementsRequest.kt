package com.depromeet.threedollar.api.core.service.user.advertisement.dto.request

import javax.validation.constraints.NotNull
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPositionType

data class RetrieveAdvertisementsRequest(
    @field:NotNull(message = "{advertisement.platform.notnull}")
    val platform: AdvertisementPlatformType?,

    @field:NotNull(message = "{advertisement.position.notnull}")
    val position: AdvertisementPositionType = AdvertisementPositionType.SPLASH,
)
