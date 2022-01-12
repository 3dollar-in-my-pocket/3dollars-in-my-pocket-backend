package com.depromeet.threedollar.domain.user.domain.admin;

import com.depromeet.threedollar.domain.user.domain.admin.repository.AdminRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

}
