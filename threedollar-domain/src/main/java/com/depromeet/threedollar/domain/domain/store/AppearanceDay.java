package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AppearanceDay extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private DayOfTheWeek day;

    private AppearanceDay(Store store, DayOfTheWeek day) {
        this.store = store;
        this.day = day;
    }

    static AppearanceDay of(Store store, DayOfTheWeek day) {
        return new AppearanceDay(store, day);
    }

}

