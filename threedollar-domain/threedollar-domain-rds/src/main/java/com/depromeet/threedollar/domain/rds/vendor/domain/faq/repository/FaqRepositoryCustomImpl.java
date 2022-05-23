package com.depromeet.threedollar.domain.rds.vendor.domain.faq.repository;

import static com.depromeet.threedollar.domain.rds.common.support.QuerydslSupport.predicate;
import static com.depromeet.threedollar.domain.rds.vendor.domain.faq.QFaq.faq;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.vendor.domain.faq.Faq;
import com.depromeet.threedollar.domain.rds.vendor.domain.faq.FaqCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FaqRepositoryCustomImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Faq> findAllByCategory(FaqCategory category) {
        return queryFactory.selectFrom(faq)
            .where(
                predicate(category != null, () -> faq.category.eq(category))
            ).fetch();
    }

    @Nullable
    @Override
    public Faq findFaqById(long faqId) {
        return queryFactory.selectFrom(faq)
            .where(
                faq.id.eq(faqId)
            ).fetchOne();
    }

}

