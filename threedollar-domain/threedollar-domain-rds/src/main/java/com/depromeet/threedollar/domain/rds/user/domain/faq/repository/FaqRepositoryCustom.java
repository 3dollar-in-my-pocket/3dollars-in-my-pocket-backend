package com.depromeet.threedollar.domain.rds.user.domain.faq.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq;
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory;

public interface FaqRepositoryCustom {

    List<Faq> findAllByCategory(FaqCategory category);

    @Nullable
    Faq findFaqById(long faqId);

}
