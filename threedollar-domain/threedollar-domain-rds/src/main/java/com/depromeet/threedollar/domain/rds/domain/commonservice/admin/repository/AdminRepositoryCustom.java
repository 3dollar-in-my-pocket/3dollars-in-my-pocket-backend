package com.depromeet.threedollar.domain.rds.domain.commonservice.admin.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin;

public interface AdminRepositoryCustom {

    @Nullable
    Admin findAdminById(Long id);

    @Nullable
    Admin findAdminByEmail(String email);

    boolean existsByEmail(String email);

    List<Admin> findAllWithPagination(long page, int size);

}