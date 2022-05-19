package com.depromeet.threedollar.common.utils;

import static com.depromeet.threedollar.common.config.jackson.ExternalIdJsonModule.decodeIdModule;
import static com.depromeet.threedollar.common.config.jackson.JavaTimeJsonModule.javaTimeModule;
import static com.depromeet.threedollar.common.config.jackson.StringJsonModule.stringJsonModule;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .registerModules(javaTimeModule(), decodeIdModule(), stringJsonModule(), new ParameterNamesModule(), new Jdk8Module(), new KotlinModule());

    @NotNull
    public static <T> T toObject(@NotNull String input, Class<T> toClass) {
        try {
            return OBJECT_MAPPER.readValue(input, toClass);
        } catch (IOException e) {
            throw new InternalServerException(String.format("역직렬화 중 에러가 발생하였습니다. input: (%s) toClass: (%s) message: (%s)", input, toClass.getSimpleName(), e.getMessage()));
        }
    }

    @NotNull
    public static <T> String toJson(@NotNull T input) {
        try {
            return OBJECT_MAPPER.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new InternalServerException(String.format("직렬화 중 에러가 발생하였습니다. input: (%s) message: (%s)", input, e.getMessage()));
        }
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

}
