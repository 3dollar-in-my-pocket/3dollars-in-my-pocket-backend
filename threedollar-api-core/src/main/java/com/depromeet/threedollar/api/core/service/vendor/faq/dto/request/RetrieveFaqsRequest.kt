package com.depromeet.threedollar.api.core.service.vendor.faq.dto.request

import com.depromeet.threedollar.domain.rds.vendor.domain.faq.FaqCategory

data class RetrieveFaqsRequest(
    val category: FaqCategory?,
)
