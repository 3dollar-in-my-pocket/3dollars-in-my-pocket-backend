package com.depromeet.threedollar.domain.domain.menu;

import com.depromeet.threedollar.domain.domain.menu.repository.MenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

}
