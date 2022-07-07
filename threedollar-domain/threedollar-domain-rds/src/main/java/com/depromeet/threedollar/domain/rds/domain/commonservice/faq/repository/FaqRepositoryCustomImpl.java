package com.depromeet.threedollar.domain.rds.domain.commonservice.faq.repository;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq;
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.depromeet.threedollar.domain.rds.core.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.domain.commonservice.faq.QFaq.faq;

@RequiredArgsConstructor
public class FaqRepositoryCustomImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Faq> findAllByApplicationTypeAndCategory(@Nullable ApplicationType applicationType, @Nullable FaqCategory category) {
        return queryFactory.selectFrom(faq)
            .where(
                predicate(applicationType != null, () -> faq.applicationType.eq(applicationType)),
                predicate(category != null, () -> faq.category.eq(category))
            ).fetch();
    }

    @Nullable
    @Override
    public Faq findFaqById(Long faqId) {
        return queryFactory.selectFrom(faq)
            .where(
                faq.id.eq(faqId)
            ).fetchOne();
    }

}

