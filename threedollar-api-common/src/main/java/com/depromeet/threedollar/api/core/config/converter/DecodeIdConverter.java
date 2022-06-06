package com.depromeet.threedollar.api.core.config.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.model.ExternalId;

@Component
public class DecodeIdConverter implements Converter<String, ExternalId> {

    @Override
    public ExternalId convert(@NotNull String source) {
        return ExternalId.toExternal(source);
    }

}
