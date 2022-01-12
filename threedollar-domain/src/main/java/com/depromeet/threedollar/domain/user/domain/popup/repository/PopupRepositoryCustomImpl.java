package com.depromeet.threedollar.domain.user.domain.popup.repository;

import com.depromeet.threedollar.domain.user.domain.popup.Popup;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPositionType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.depromeet.threedollar.domain.user.domain.popup.QPopup.popup;

@RequiredArgsConstructor
public class PopupRepositoryCustomImpl implements PopupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Popup> findActivatedPopupsByPositionAndPlatform(PopupPositionType positionType, PopupPlatformType platformType, LocalDateTime dateTime) {
        return queryFactory.selectFrom(popup)
            .where(
                popup.positionType.eq(positionType),
                popup.platformType.eq(platformType),
                popup.dateTimeInterval.startDateTime.loe(dateTime),
                popup.dateTimeInterval.endDateTime.goe(dateTime)
            )
            .orderBy(popup.id.desc())
            .fetch();
    }

}
