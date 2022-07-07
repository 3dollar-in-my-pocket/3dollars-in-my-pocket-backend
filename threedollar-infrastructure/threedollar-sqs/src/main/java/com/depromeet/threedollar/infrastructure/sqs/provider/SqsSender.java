package com.depromeet.threedollar.infrastructure.sqs.provider;

import com.depromeet.threedollar.common.utils.JsonUtils;
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;
import com.depromeet.threedollar.infrastructure.sqs.utils.SqsTopicFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Component
public class SqsSender {

    private final QueueMessagingTemplate queueMessagingTemplate;

    public void sendToTopic(@NotNull TopicType topicType, @NotNull Object payload) {
        Message<String> message = MessageBuilder
            .withPayload(JsonUtils.toJson(payload))
            .build();
        queueMessagingTemplate.send(SqsTopicFinder.getTopicName(topicType), message);
    }

}
