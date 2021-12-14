package com.depromeet.threedollar.domain.domain.medal;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Medal extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Embedded
    private MedalImage medalImage;

    @Column(length = 200)
    private String introduction;

    @OneToOne(mappedBy = "medal", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedalAcquisitionCondition acquisitionCondition;

    @Builder(access = AccessLevel.PACKAGE)
    private Medal(String name, String introduction, String activationIconUrl, String disableIconUrl, MedalAcquisitionConditionType conditionType, int count, String acquisitionDescription) {
        this.name = name;
        this.introduction = introduction;
        this.medalImage = MedalImage.of(activationIconUrl, disableIconUrl);
        this.acquisitionCondition = MedalAcquisitionCondition.of(this, conditionType, count, acquisitionDescription);
    }

    public boolean canObtain(MedalAcquisitionConditionType conditionType, long counts) {
        return this.acquisitionCondition.canObtain(conditionType, counts);
    }

    @NotNull
    public String getActivationIconUrl() {
        return this.medalImage.getActivationIconUrl();
    }

    @NotNull
    public String getDisableIconUrl() {
        return this.medalImage.getDisableIconUrl();
    }

}
