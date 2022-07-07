package com.depromeet.threedollar.infrastructure.sqs.provider;

import com.depromeet.threedollar.infrastructure.sqs.common.type.TopicType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Profile("integration-test")
@Component
public class DummyMessageSendProvider implements MessageSendProvider {

    @Override
    public void sendToTopic(TopicType topicType, Object payload) {
        log.info("메시지가 더미 토픽으로 전송됩니다 topicType: {} payload: {}", topicType, payload);
    }

}
