package com.depromeet.threedollar.infrastructure.sqs.dto.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.common.type.PushOptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFirebaseMessagePayload {

    @NotBlank
    private String token;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    private PushOptions pushOptions;

}
