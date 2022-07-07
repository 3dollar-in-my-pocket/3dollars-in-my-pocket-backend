package com.depromeet.threedollar.common.config.jackson;

import com.depromeet.threedollar.common.model.DecodedId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecodedIdJsonModule {

    public static SimpleModule decodeIdModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(DecodedId.class, new DecodedIdSerializer());
        simpleModule.addDeserializer(DecodedId.class, new DecodedIdDeserializer());
        return simpleModule;
    }

    private static class DecodedIdSerializer extends JsonSerializer<DecodedId> {

        @Override
        public void serialize(@NotNull DecodedId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getExternalId());
        }

    }

    private static class DecodedIdDeserializer extends JsonDeserializer<DecodedId> {

        @Override
        public DecodedId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String encodedId = p.getValueAsString();
            return DecodedId.toExternal(encodedId);
        }

    }

}
