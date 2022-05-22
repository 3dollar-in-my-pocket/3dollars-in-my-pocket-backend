package com.depromeet.threedollar.common.config.jackson;

import java.io.IOException;

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
public class StringJsonModule {

    public static SimpleModule stringJsonModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(String.class, new StringSerializer());
        simpleModule.addDeserializer(String.class, new StringDeserializer());
        return simpleModule;
    }

    private static class StringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
            String value = p.getValueAsString();
            if (value == null) {
                return null;
            }
            return value.trim();
        }

    }

    private static class StringSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            gen.writeString(value.trim());
        }

    }

}
