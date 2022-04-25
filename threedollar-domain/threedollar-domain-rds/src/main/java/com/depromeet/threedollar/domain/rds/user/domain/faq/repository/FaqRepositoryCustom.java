package com.depromeet.threedollar.domain.rds.user.domain.faq.repository;

import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FaqRepositoryCustom {

    List<Faq> findAllByCategory(FaqCategory category);

    @Nullable
    Faq findFaqById(long faqId);

}
