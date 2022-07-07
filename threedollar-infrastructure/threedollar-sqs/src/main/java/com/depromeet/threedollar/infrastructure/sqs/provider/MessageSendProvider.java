package com.depromeet.threedollar.infrastructure.sqs.provider;

import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;

import javax.validation.constraints.NotNull;

public interface MessageSendProvider {

    void sendToTopic(@NotNull TopicType topicType, @NotNull Object payload);

}
