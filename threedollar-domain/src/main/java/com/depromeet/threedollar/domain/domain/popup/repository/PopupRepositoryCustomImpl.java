package com.depromeet.threedollar.domain.domain.popup.repository;

import com.depromeet.threedollar.domain.domain.popup.Popup;
import com.depromeet.threedollar.domain.domain.popup.PopupPlatformType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.popup.QPopup.popup;

@RequiredArgsConstructor
public class PopupRepositoryCustomImpl implements PopupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Popup> findActivatedPopupsByPlatform(PopupPlatformType platformType, LocalDateTime dateTime) {
        return queryFactory.selectFrom(popup)
            .where(
                popup.platformType.eq(platformType),
                popup.dateTimeInterval.startDateTime.loe(dateTime),
                popup.dateTimeInterval.endDateTime.goe(dateTime)
            )
            .orderBy(popup.id.desc())
            .limit(1) // 일단 팝업 한개만으로 제한
            .fetch();
    }

}
