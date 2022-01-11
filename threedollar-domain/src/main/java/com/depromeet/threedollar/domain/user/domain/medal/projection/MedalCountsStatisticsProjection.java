package com.depromeet.threedollar.domain.user.domain.medal.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MedalCountsStatisticsProjection {

    private final String medalName;
    private final long counts;

    @QueryProjection
    public MedalCountsStatisticsProjection(String medalName, long counts) {
        this.medalName = medalName;
        this.counts = counts;
    }

}
