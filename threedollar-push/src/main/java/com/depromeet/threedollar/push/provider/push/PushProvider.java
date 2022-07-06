package com.depromeet.threedollar.push.provider.push;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessageBulkPayload;
import com.depromeet.threedollar.infrastructure.sqs.dto.payload.SendFirebaseMessagePayload;

public interface PushProvider {

    void sendMessageAsync(@NotNull ApplicationType applicationType, @NotNull SendFirebaseMessagePayload request);

    void sendMessageBulkAsync(@NotNull ApplicationType applicationType, @NotNull SendFirebaseMessageBulkPayload request);

}
