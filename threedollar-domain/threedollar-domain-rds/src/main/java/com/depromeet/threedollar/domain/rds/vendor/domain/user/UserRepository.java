package com.depromeet.threedollar.domain.rds.vendor.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.vendor.domain.user.repository.UserRepositoryCustom;
import com.depromeet.threedollar.domain.rds.vendor.domain.user.repository.UserStatisticsRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, UserStatisticsRepositoryCustom {

}
