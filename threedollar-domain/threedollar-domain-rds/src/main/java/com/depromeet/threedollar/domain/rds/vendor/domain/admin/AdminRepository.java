package com.depromeet.threedollar.domain.rds.vendor.domain.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.admin.repository.AdminRepositoryCustom;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

}
