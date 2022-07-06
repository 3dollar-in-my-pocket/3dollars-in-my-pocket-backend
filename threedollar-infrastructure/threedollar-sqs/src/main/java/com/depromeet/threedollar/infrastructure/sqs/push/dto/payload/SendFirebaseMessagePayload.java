package com.depromeet.threedollar.infrastructure.sqs.push.dto.payload;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFirebaseMessagePayload {

    @NotBlank
    private String token;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

}
