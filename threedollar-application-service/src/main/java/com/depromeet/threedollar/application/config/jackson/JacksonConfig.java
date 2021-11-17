package com.depromeet.threedollar.application.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        objectMapper.registerModules(javaTimeModule, new ParameterNamesModule(), new KotlinModule());
        return objectMapper;
    }

    private static class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(LOCAL_DATE_FORMATTER));
        }

    }

    private static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return LocalDate.parse(p.getValueAsString(), LOCAL_DATE_FORMATTER);
        }

    }

    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(LOCAL_DATE_TIME_FORMATTER));
        }

    }

    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), LOCAL_DATE_TIME_FORMATTER);
        }

    }

    private static class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(LOCAL_TIME_FORMATTER));
        }

    }

    private static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

        @Override
        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalTime.parse(p.getValueAsString(), LOCAL_TIME_FORMATTER);
        }
    }

}
