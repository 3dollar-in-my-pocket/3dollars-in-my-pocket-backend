package com.depromeet.threedollar.domain.rds.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.user.repository.UserRepositoryCustom;
import com.depromeet.threedollar.domain.rds.user.domain.user.repository.UserStatisticsRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, UserStatisticsRepositoryCustom {

}
