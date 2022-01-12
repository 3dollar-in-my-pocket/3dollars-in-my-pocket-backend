package com.depromeet.threedollar.domain.user.domain.popup;

import com.depromeet.threedollar.domain.user.domain.popup.repository.PopupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Long>, PopupRepositoryCustom {

}
