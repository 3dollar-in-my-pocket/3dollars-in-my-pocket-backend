package com.depromeet.threedollar.common.config.jackson;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.model.ExternalId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalIdJsonModule {

    public static SimpleModule decodeIdModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ExternalId.class, new ExternalIdSerializer());
        simpleModule.addDeserializer(ExternalId.class, new ExternalIdDeserializer());
        return simpleModule;
    }

    private static class ExternalIdSerializer extends JsonSerializer<ExternalId> {

        @Override
        public void serialize(@NotNull ExternalId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getExternalId());
        }

    }

    private static class ExternalIdDeserializer extends JsonDeserializer<ExternalId> {

        @Override
        public ExternalId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String encodedId = p.getValueAsString();
            return ExternalId.toExternal(encodedId);
        }

    }

}
