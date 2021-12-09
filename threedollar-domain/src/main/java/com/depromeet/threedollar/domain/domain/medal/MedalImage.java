package com.depromeet.threedollar.domain.domain.medal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
class MedalImage {

    @Column(nullable = false)
    private String activationIconUrl;

    @Column(nullable = false)
    private String disableIconUrl;

    static MedalImage of(String activationIconUrl, String disableIconUrl) {
        return new MedalImage(activationIconUrl, disableIconUrl);
    }

}
