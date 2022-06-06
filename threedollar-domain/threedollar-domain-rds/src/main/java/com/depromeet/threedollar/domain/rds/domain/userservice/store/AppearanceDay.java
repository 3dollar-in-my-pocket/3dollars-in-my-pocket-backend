package com.depromeet.threedollar.domain.rds.domain.userservice.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private AppearanceDay(@NotNull Store store, @NotNull DayOfTheWeek day) {
        this.store = store;
        this.day = day;
    }

    static AppearanceDay of(@NotNull Store store, @NotNull DayOfTheWeek day) {
        return new AppearanceDay(store, day);
    }

}

