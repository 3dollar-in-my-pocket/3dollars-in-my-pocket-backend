package com.depromeet.threedollar.domain.rds.vendor.domain.medal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uni_medal_acquisition_condition_1", columnNames = {"medal_id", "conditionType"}),
    }
)
public class MedalAcquisitionCondition extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medal_id", nullable = false)
    private Medal medal;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private MedalAcquisitionConditionType conditionType;

    @Column(nullable = false)
    private int count;

    @Column(length = 200)
    private String description;

    @Builder(access = AccessLevel.PACKAGE)
    private MedalAcquisitionCondition(@NotNull Medal medal, @NotNull MedalAcquisitionConditionType conditionType, int count, @Nullable String description) {
        this.medal = medal;
        this.conditionType = conditionType;
        this.count = count;
        this.description = description;
    }

    static MedalAcquisitionCondition of(@NotNull Medal medal, @NotNull MedalAcquisitionConditionType conditionType, int count, @Nullable String description) {
        return new MedalAcquisitionCondition(medal, conditionType, count, description);
    }

    boolean canObtain(@NotNull MedalAcquisitionConditionType conditionType, long counts) {
        return this.conditionType.equals(conditionType) && this.count <= counts;
    }

}
