package com.depromeet.threedollar.domain.rds.user.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.MenuStaticsRepositoryCustom;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuStaticsRepositoryCustom {

}
