package com.depromeet.threedollar.foodtruck.api.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModules(javaTimeModule(), ParameterNamesModule(), KotlinModule())
        return objectMapper
    }

    private fun javaTimeModule(): JavaTimeModule {
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
        javaTimeModule.addSerializer(LocalDate::class.java, LocalDateSerializer())
        javaTimeModule.addDeserializer(LocalDate::class.java, LocalDateDeserializer())
        javaTimeModule.addSerializer(LocalTime::class.java, LocalTimeSerializer())
        javaTimeModule.addDeserializer(LocalTime::class.java, LocalTimeDeserializer())
        return javaTimeModule
    }

    private class LocalDateSerializer : JsonSerializer<LocalDate>() {
        override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString(value.format(LOCAL_DATE_FORMATTER))
        }
    }

    private class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
        override fun deserialize(p: JsonParser, context: DeserializationContext): LocalDate {
            return LocalDate.parse(p.valueAsString, LOCAL_DATE_FORMATTER)
        }
    }

    private class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
        override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString(value.format(LOCAL_DATE_TIME_FORMATTER))
        }
    }

    private class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
        override fun deserialize(p: JsonParser, context: DeserializationContext): LocalDateTime {
            return LocalDateTime.parse(p.valueAsString, LOCAL_DATE_TIME_FORMATTER)
        }
    }

    private class LocalTimeSerializer : JsonSerializer<LocalTime>() {
        override fun serialize(value: LocalTime, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString(value.format(LOCAL_TIME_FORMATTER))
        }
    }

    private class LocalTimeDeserializer : JsonDeserializer<LocalTime>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalTime {
            return LocalTime.parse(p.valueAsString, LOCAL_TIME_FORMATTER)
        }
    }

    companion object {
        private val LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
        private val LOCAL_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME
        private val LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    }

}
