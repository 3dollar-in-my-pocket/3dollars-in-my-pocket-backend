package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Medal extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String iconUrl;

    @OneToOne(mappedBy = "medal", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedalAcquisitionCondition acquisitionCondition;

    Medal(String name, String iconUrl, MedalAcquisitionConditionType conditionType, int count) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.acquisitionCondition = MedalAcquisitionCondition.of(this, conditionType, count);
    }

    public boolean canObtain(MedalAcquisitionConditionType conditionType, long counts) {
        return this.acquisitionCondition.canObtain(conditionType, counts);
    }

}
