package com.depromeet.threedollar.domain.domain.popup.repository;

import com.depromeet.threedollar.domain.domain.popup.Popup;
import com.depromeet.threedollar.domain.domain.popup.PopupPlatformType;

import java.time.LocalDateTime;
import java.util.List;

public interface PopupRepositoryCustom {

    List<Popup> findActivatedPopupsByPlatform(PopupPlatformType platformType, LocalDateTime dateTime);

}
