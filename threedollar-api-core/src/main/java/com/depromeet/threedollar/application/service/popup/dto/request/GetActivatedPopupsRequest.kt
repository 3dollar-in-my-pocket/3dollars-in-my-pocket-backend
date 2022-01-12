package com.depromeet.threedollar.application.service.popup.dto.request

import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType
import javax.validation.constraints.NotNull

data class GetActivatedPopupsRequest(
    @field:NotNull(message = "{popup.platform.notnull}")
    val platform: PopupPlatformType?,

    @field:NotNull(message = "{popup.position.notnull}")
    val position: PopupPositionType = PopupPositionType.SPLASH
)