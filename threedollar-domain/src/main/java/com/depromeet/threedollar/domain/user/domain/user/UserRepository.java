package com.depromeet.threedollar.domain.user.domain.user;

import com.depromeet.threedollar.domain.user.domain.user.repository.UserRepositoryCustom;
import com.depromeet.threedollar.domain.user.domain.user.repository.UserStatisticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, UserStatisticsRepositoryCustom {

}
