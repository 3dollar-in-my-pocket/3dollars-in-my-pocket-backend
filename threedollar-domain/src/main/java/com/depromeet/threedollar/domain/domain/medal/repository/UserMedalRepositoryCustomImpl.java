package com.depromeet.threedollar.domain.domain.medal.repository;

import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import static com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.USER_MEDALS_COUNTS;
import static com.depromeet.threedollar.domain.domain.medal.QUserMedal.userMedal;

@RequiredArgsConstructor
public class UserMedalRepositoryCustomImpl implements UserMedalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsMedalByUserId(Long userId, UserMedalType medalType) {
        return queryFactory.selectOne()
            .from(userMedal)
            .where(
                userMedal.userId.eq(userId),
                userMedal.medalType.eq(medalType)
            ).fetchFirst() != null;
    }

    @Override
    public List<UserMedalType> findAllUserMedalTypeByUserId(Long userId) {
        return queryFactory.select(userMedal.medalType)
            .from(userMedal)
            .where(
                userMedal.userId.eq(userId)
            ).fetch();
    }

    @Cacheable(key = "#userId", value = USER_MEDALS_COUNTS)
    @Override
    public long findCountsByUserId(Long userId) {
        return queryFactory.selectOne()
            .from(userMedal)
            .where(
                userMedal.userId.eq(userId)
            ).fetchCount();
    }

}
