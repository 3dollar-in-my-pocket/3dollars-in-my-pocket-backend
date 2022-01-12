package com.depromeet.threedollar.domain.boss.domain;

import lombok.*;

import javax.persistence.Embeddable;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PhoneInfo {

    private String front;

    private String middle;

    private String back;

}
