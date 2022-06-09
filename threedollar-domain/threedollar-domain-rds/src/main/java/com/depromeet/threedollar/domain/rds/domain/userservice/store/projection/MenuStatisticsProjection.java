package com.depromeet.threedollar.domain.rds.domain.userservice.store.projection;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MenuStatisticsProjection {

    private final UserMenuCategoryType category;
    private final long counts;

    @QueryProjection
    public MenuStatisticsProjection(UserMenuCategoryType category, long counts) {
        this.category = category;
        this.counts = counts;
    }

}
