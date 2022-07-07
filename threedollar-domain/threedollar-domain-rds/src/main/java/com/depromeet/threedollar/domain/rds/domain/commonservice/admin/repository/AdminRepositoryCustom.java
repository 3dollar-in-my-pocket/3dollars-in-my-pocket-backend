package com.depromeet.threedollar.domain.rds.domain.commonservice.admin.repository;

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.Admin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AdminRepositoryCustom {

    @Nullable
    Admin findAdminById(Long id);

    @Nullable
    Admin findAdminByEmail(String email);

    boolean existsAdminByEmail(String email);

    boolean existsAdminById(Long id);

    List<Admin> findAllWithPagination(long page, int size);

}
