package com.depromeet.threedollar.domain.rds.user.domain.medal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MedalImage {

    @Column(nullable = false, length = 2048)
    private String activationIconUrl;

    @Column(nullable = false, length = 2048)
    private String disableIconUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private MedalImage(String activationIconUrl, String disableIconUrl) {
        this.activationIconUrl = activationIconUrl;
        this.disableIconUrl = disableIconUrl;
    }

    @Builder(toBuilder = true)
    static MedalImage of(String activationIconUrl, String disableIconUrl) {
        return MedalImage.builder()
            .activationIconUrl(activationIconUrl)
            .disableIconUrl(disableIconUrl)
            .build();
    }

}
