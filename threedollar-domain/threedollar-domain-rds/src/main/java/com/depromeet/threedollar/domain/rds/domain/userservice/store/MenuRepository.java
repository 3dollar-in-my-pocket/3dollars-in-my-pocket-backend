package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.MenuStaticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuStaticsRepositoryCustom {

}
