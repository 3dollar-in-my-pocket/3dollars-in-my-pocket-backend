package com.depromeet.threedollar.api.userservice.controller.faq.support;

import com.depromeet.threedollar.api.core.service.service.commonservice.faq.dto.response.FaqResponse;
import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@TestAssertions
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FaqAssertions {

    public static void assertFaqResponse(FaqResponse faqResponse, Long id, String question, String answer, FaqCategory category) {
        assertThat(faqResponse.getFaqId()).isEqualTo(id);
        assertThat(faqResponse.getQuestion()).isEqualTo(question);
        assertThat(faqResponse.getAnswer()).isEqualTo(answer);
        assertThat(faqResponse.getCategory()).isEqualTo(category);
    }

}
