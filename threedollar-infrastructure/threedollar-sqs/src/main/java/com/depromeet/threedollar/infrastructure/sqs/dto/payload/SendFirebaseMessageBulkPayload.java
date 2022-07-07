package com.depromeet.threedollar.infrastructure.sqs.dto.payload;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class SendFirebaseMessageBulkPayload {

    @Valid
    @Size(min = 1, max = 500)
    private Set<String> tokens;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    private PushOptions pushOptions;

}
