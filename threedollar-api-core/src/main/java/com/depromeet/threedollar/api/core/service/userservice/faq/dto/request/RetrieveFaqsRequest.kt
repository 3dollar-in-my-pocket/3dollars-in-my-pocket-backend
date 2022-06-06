package com.depromeet.threedollar.api.core.service.userservice.faq.dto.request

import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory

data class RetrieveFaqsRequest(
    val category: FaqCategory?,
)
