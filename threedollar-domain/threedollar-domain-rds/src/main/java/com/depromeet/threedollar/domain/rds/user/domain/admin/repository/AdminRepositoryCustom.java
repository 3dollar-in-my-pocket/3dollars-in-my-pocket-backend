package com.depromeet.threedollar.domain.rds.user.domain.admin.repository;

import com.depromeet.threedollar.domain.rds.user.domain.admin.Admin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AdminRepositoryCustom {

    @Nullable
    Admin findAdminById(Long id);

    @Nullable
    Admin findAdminByEmail(String email);

    boolean existsByEmail(String email);

    List<Admin> findAllWithPagination(long page, int size);

}
