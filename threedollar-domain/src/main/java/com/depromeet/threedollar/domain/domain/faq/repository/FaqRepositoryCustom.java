package com.depromeet.threedollar.domain.domain.faq.repository;

import com.depromeet.threedollar.domain.domain.faq.Faq;
import com.depromeet.threedollar.domain.domain.faq.FaqCategory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FaqRepositoryCustom {

    List<Faq> findAllByCategory(FaqCategory category);

    @Nullable
    Faq findFaqById(long faqId);

}
