package com.depromeet.threedollar.domain.rds.domain.commonservice.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.commonservice.admin.repository.AdminRepositoryCustom;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

}