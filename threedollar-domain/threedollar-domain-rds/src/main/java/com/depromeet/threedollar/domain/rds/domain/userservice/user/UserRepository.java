package com.depromeet.threedollar.domain.rds.domain.userservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.repository.UserRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.repository.statistics.UserStatisticsRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, UserStatisticsRepositoryCustom {

}
