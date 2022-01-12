package com.depromeet.threedollar.domain.user.domain.popup.repository;

import com.depromeet.threedollar.domain.user.domain.popup.Popup;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType;

import java.time.LocalDateTime;
import java.util.List;

public interface PopupRepositoryCustom {

    List<Popup> findActivatedPopupsByPositionAndPlatform(PopupPositionType positionType, PopupPlatformType platformType, LocalDateTime dateTime);

}
