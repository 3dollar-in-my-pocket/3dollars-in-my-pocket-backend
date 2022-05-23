package com.depromeet.threedollar.api.vendor.controller.faq.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.depromeet.threedollar.api.core.service.vendor.faq.dto.response.FaqResponse;
import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.faq.FaqCategory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FaqAssertions {

    public static void assertFaqResponse(FaqResponse faqResponse, Long id, String question, String answer, FaqCategory category) {
        assertThat(faqResponse.getFaqId()).isEqualTo(id);
        assertThat(faqResponse.getQuestion()).isEqualTo(question);
        assertThat(faqResponse.getAnswer()).isEqualTo(answer);
        assertThat(faqResponse.getCategory()).isEqualTo(category);
    }

}
