package com.depromeet.threedollar.external.client.apple;

import org.jetbrains.annotations.NotNull;

public interface AppleTokenProvider {

    String getSocialIdFromIdToken(@NotNull String idToken);

}
