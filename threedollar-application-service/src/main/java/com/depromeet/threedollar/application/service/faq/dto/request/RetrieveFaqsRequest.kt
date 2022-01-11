package com.depromeet.threedollar.application.service.faq.dto.request

import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory

data class RetrieveFaqsRequest(
    val category: FaqCategory?
)
