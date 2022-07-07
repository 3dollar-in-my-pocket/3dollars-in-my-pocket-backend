package com.depromeet.threedollar.domain.rds.domain.userservice.user;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.repository.UserRepositoryCustom;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.repository.statistics.UserStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, UserStatisticsRepositoryCustom {

}
