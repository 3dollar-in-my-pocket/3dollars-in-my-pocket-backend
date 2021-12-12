package com.depromeet.threedollar.domain.domain.admin.repository;

import com.depromeet.threedollar.domain.domain.admin.Admin;
import org.jetbrains.annotations.Nullable;

public interface AdminRepositoryCustom {

    @Nullable
    Admin findAdminById(Long id);

    @Nullable
    Admin findAdminByEmail(String email);

}
