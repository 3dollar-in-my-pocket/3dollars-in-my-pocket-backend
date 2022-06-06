package com.depromeet.threedollar.domain.rds.domain.commonservice.admin.repository;

import static com.depromeet.threedollar.domain.rds.domain.commonservice.admin.QAdmin.admin;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Nullable
    @Override
    public Admin findAdminById(Long id) {
        return queryFactory.selectFrom(admin)
            .where(
                admin.id.eq(id)
            ).fetchOne();
    }

    @Nullable
    @Override
    public Admin findAdminByEmail(String email) {
        return queryFactory.selectFrom(admin)
            .where(
                admin.email.email.eq(email)
            ).fetchOne();
    }

    @Override
    public boolean existsByEmail(String email) {
        return queryFactory.selectOne()
            .from(admin)
            .where(
                admin.email.email.eq(email)
            ).fetchFirst() != null;
    }

    @Override
    public List<Admin> findAllWithPagination(long page, int size) {
        return queryFactory.selectFrom(admin)
            .orderBy(admin.id.desc())
            .offset(page * size)
            .limit(size)
            .fetch();
    }

}
