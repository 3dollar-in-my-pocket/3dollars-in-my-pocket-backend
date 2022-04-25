package com.depromeet.threedollar.domain.rds.user.domain.store;

import com.depromeet.threedollar.domain.rds.user.domain.store.repository.MenuStaticsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuStaticsRepositoryCustom {

}
