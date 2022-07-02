package com.depromeet.threedollar.api.core.config.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.model.DecodedId;

@Component
public class DecodeIdConverter implements Converter<String, DecodedId> {

    @Override
    public DecodedId convert(@NotNull String source) {
        return DecodedId.toExternal(source);
    }

}
