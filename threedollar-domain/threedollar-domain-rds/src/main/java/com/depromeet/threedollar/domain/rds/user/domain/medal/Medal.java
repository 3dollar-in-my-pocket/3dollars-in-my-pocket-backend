package com.depromeet.threedollar.domain.rds.user.domain.medal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    private Medal(String name, String introduction, String activationIconUrl, String disableIconUrl,
                  MedalAcquisitionConditionType conditionType, int conditionCount, String acquisitionDescription) {
        this.name = name;
        this.introduction = introduction;
        this.medalImage = MedalImage.of(activationIconUrl, disableIconUrl);
        this.acquisitionCondition = MedalAcquisitionCondition.of(this, conditionType, conditionCount, acquisitionDescription);
    }

    public static Medal newInstance(String name, String introduction, String activationIconUrl, String disableIconUrl,
                                    MedalAcquisitionConditionType conditionType, int conditionCount, String acquisitionDescription) {
        return Medal.builder()
            .name(name)
            .introduction(introduction)
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .conditionType(conditionType)
            .conditionCount(conditionCount)
            .acquisitionDescription(acquisitionDescription)
            .build();
    }

    public void update(String name, @Nullable String introduction, String activationIconUrl, String disableIconUrl) {
        this.name = name;
        this.introduction = introduction;
        this.medalImage = MedalImage.of(activationIconUrl, disableIconUrl);
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
