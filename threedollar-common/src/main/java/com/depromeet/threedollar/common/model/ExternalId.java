package com.depromeet.threedollar.common.model;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.utils.Base32Utils;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalId {

    private Long internalId;
    private String externalId;

    @Builder(access = AccessLevel.PRIVATE)
    private ExternalId(String externalId, Long internalId) {
        this.externalId = externalId;
        this.internalId = internalId;
    }

    public static ExternalId toInternal(@NotNull Long originId) {
        return ExternalId.builder()
            .internalId(originId)
            .externalId(Base32Utils.encode(originId))
            .build();
    }

    public static ExternalId toExternal(@NotNull String encodingId) {
        return ExternalId.builder()
            .internalId(Base32Utils.decode(encodingId))
            .externalId(encodingId)
            .build();
    }

}
