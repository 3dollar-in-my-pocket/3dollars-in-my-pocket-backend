package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MedalAcquisitionConditionType conditionType;

    @Column(nullable = false)
    private int count;

    private MedalAcquisitionCondition(Medal medal, MedalAcquisitionConditionType conditionType, int count) {
        this.medal = medal;
        this.conditionType = conditionType;
        this.count = count;
    }

    static MedalAcquisitionCondition of(Medal medal, MedalAcquisitionConditionType conditionType, int count) {
        return new MedalAcquisitionCondition(medal, conditionType, count);
    }

    boolean canObtain(MedalAcquisitionConditionType conditionType, long counts) {
        return this.conditionType.equals(conditionType) && this.count <= counts;
    }

}
