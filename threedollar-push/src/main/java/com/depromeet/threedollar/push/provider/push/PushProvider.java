package com.depromeet.threedollar.push.provider.push;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendBulkPushRequest;
import com.depromeet.threedollar.infrastructure.sqs.provider.dto.request.SendSinglePushRequest;
import org.jetbrains.annotations.NotNull;

public interface PushProvider {

    void sendMessageAsync(@NotNull ApplicationType applicationType, @NotNull SendSinglePushRequest request);

    void sendMessageBulkAsync(@NotNull ApplicationType applicationType, @NotNull SendBulkPushRequest request);

}
