package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.repository.statistics.MenuStaticsRepositoryCustom;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuStaticsRepositoryCustom {

}
