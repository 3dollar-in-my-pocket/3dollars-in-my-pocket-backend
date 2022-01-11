package com.depromeet.threedollar.domain.user.domain.admin.repository;

import com.depromeet.threedollar.domain.user.domain.admin.Admin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import static com.depromeet.threedollar.domain.user.domain.admin.QAdmin.admin;

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
                admin.email.eq(email)
            ).fetchOne();
    }

}
