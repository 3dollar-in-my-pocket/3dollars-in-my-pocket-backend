package com.depromeet.threedollar.api.core.service.user.faq.dto.request

import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory

data class RetrieveFaqsRequest(
    val category: FaqCategory?,
)
