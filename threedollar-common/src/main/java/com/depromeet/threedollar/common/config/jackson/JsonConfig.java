package com.depromeet.threedollar.common.config.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.depromeet.threedollar.common.config.jackson.DecodedIdJsonModule.decodeIdModule;
import static com.depromeet.threedollar.common.config.jackson.JavaTimeJsonModule.javaTimeModule;
import static com.depromeet.threedollar.common.config.jackson.StringJsonModule.stringJsonModule;

@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .registerModules(javaTimeModule(), decodeIdModule(), stringJsonModule(), new ParameterNamesModule(), new Jdk8Module(), new KotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}

