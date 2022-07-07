package com.depromeet.threedollar.api.adminservice.service.commonservice.advertisement.dto.request

import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPositionType
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RetrieveAdvertisementsRequest(
    val applicationType: ApplicationType,

    @field:Min(value = 1, message = "{common.size.min}")
    @field:Max(value = 30, message = "{common.size.max}")
    val size: Long = 1,

    @field:Min(value = 1, message = "{common.page.min}")
    val page: Int = -1,

    val platform: AdvertisementPlatformType?,

    val position: AdvertisementPositionType?,
)
