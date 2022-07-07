package com.depromeet.threedollar.domain.rds.domain.commonservice.admin;

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.repository.AdminRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

}
