package com.depromeet.threedollar.infrastructure.sqs.utils;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

import com.depromeet.threedollar.common.context.ApplicationContextProvider;
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqsTopicFinder {

    public static final Map<TopicType, String> SQS_TOPIC_MAP = new EnumMap<>(TopicType.class);

    static {
        Environment env = ApplicationContextProvider.getApplicationContext().getEnvironment();
        SQS_TOPIC_MAP.put(TopicType.SINGLE_PUSH, env.getProperty("push.sqs.boss.single-push"));
        SQS_TOPIC_MAP.put(TopicType.BULK_PUSH, env.getProperty("push.sqs.boss.bulk-push"));
    }

    @NotNull
    public static String getTopicName(@NotNull TopicType topicType) {
        return SQS_TOPIC_MAP.get(topicType);
    }

}
