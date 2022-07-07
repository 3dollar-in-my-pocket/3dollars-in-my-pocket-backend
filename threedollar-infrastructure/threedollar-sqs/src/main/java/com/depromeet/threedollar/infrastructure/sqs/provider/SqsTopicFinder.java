package com.depromeet.threedollar.infrastructure.sqs.provider;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class SqsTopicFinder {

    private static final Map<TopicType, String> SQS_TOPIC_MAP = new EnumMap<>(TopicType.class);

    private final ApplicationContext applicationContext;

    @PostConstruct
    void initialize() {
        Environment env = applicationContext.getEnvironment();
        String bossSinglePush = env.getProperty("push.sqs.boss.single-push");
        String bossBulkPush = env.getProperty("push.sqs.boss.bulk-push");

        validateInitializeSQSProperties(bossSinglePush, bossBulkPush);

        SQS_TOPIC_MAP.put(TopicType.BOSS_SINGLE_APP_PUSH, bossSinglePush);
        SQS_TOPIC_MAP.put(TopicType.BOSS_BULK_APP_PUSH, bossBulkPush);
    }

    private void validateInitializeSQSProperties(String... propertyName) {
        for (String property : propertyName) {
            if (!StringUtils.hasLength(property)) {
                throw new InternalServerException(String.format("SQS 토픽 프로퍼티 초기화 중 에러가 발생하였습니다. property: (%s)", property));
            }
        }
    }

    @NotNull
    public String getTopicName(@NotNull TopicType topicType) {
        return SQS_TOPIC_MAP.get(topicType);
    }

}
