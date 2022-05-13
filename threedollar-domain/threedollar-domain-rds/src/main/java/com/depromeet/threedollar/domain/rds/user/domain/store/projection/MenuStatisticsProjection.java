package com.depromeet.threedollar.domain.rds.user.domain.store.projection;

import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MenuStatisticsProjection {

    private final MenuCategoryType category;
    private final long counts;

    @QueryProjection
    public MenuStatisticsProjection(MenuCategoryType category, long counts) {
        this.category = category;
        this.counts = counts;
    }

}
