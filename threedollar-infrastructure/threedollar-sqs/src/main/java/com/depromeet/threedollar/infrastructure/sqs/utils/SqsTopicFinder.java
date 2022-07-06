package com.depromeet.threedollar.infrastructure.sqs.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;

import com.depromeet.threedollar.common.context.ApplicationContextProvider;
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqsTopicFinder {

    private static final Map<TopicType, String> SQS_TOPIC_MAP = new HashMap<>() {{
        Environment env = ApplicationContextProvider.getApplicationContext().getEnvironment();
        put(TopicType.SINGLE_PUSH, env.getProperty("push.sqs.boss.single-push"));
        put(TopicType.BULK_PUSH, env.getProperty("push.sqs.boss.bulk-push"));
    }};

    public static String getTopicName(TopicType topicType) {
        return SQS_TOPIC_MAP.get(topicType);
    }

}
