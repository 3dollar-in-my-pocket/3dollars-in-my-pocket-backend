package com.depromeet.threedollar.domain.rds.domain.commonservice.faq.repository;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FaqRepositoryCustom {

    List<Faq> findAllByApplicationTypeAndCategory(@Nullable ApplicationType applicationType, @Nullable FaqCategory category);

    @Nullable
    Faq findFaqById(Long faqId);

}
