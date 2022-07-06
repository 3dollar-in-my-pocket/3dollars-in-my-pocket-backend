package com.depromeet.threedollar.infrastructure.sqs.push.dto.payload;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.depromeet.threedollar.common.type.PushOptions;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendFirebaseMessageBulkPayload {

    @NotEmpty
    @Size(min = 1, max = 500)
    @JsonProperty("tokens")
    private Set<String> tokens;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    private PushOptions pushOptions;

}
