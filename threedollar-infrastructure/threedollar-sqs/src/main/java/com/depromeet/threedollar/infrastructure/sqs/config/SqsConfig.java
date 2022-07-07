package com.depromeet.threedollar.infrastructure.sqs.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class SqsConfig {

    private final AmazonSQSAsync amazonSQSAsync;
    private final ObjectMapper objectMapper;

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        queueMessageHandlerFactory.setArgumentResolvers(Collections.singletonList(
            new PayloadMethodArgumentResolver(jackson2MessageConverter()))
        );
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync);
        return queueMessageHandlerFactory;
    }

    private MappingJackson2MessageConverter jackson2MessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

}
