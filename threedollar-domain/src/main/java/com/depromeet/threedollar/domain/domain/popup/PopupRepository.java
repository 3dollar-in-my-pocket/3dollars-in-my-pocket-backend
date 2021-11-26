package com.depromeet.threedollar.domain.domain.popup;

import com.depromeet.threedollar.domain.domain.popup.repository.PopupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Long>, PopupRepositoryCustom {

}
