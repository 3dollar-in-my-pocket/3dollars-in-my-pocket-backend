package com.depromeet.threedollar.application.service.advertisement.dto.request

import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.user.domain.advertisement.AdvertisementPositionType
import javax.validation.constraints.NotNull

data class RetrieveAdvertisementsRequest(
    @field:NotNull(message = "{advertisement.platform.notnull}")
    val platform: AdvertisementPlatformType?,

    @field:NotNull(message = "{advertisement.position.notnull}")
    val position: AdvertisementPositionType = AdvertisementPositionType.SPLASH
)
