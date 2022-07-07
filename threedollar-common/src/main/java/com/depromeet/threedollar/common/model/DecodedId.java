package com.depromeet.threedollar.common.model;

import com.depromeet.threedollar.common.utils.Base32Utils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecodedId {

    private Long internalId;
    private String externalId;

    @Builder(access = AccessLevel.PRIVATE)
    private DecodedId(String externalId, Long internalId) {
        this.externalId = externalId;
        this.internalId = internalId;
    }

    public static DecodedId toInternal(@NotNull Long originId) {
        return DecodedId.builder()
            .internalId(originId)
            .externalId(Base32Utils.encode(originId))
            .build();
    }

    public static DecodedId toExternal(@NotNull String encodingId) {
        return DecodedId.builder()
            .internalId(Base32Utils.decode(encodingId))
            .externalId(encodingId)
            .build();
    }

}
