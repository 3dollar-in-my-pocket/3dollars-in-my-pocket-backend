package com.depromeet.threedollar.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ClientIpUtilsTest {

    @Test
    void X_Forwarded_For_헤더가_없는경우_Remote_Address를_반환한다() {
        // given
        String remoteAddress = "1.2.3.4";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, null);

        // then
        assertThat(clientIp).isEqualTo(remoteAddress);
    }

    @Test
    void X_Forwarded_For_헤더가_빈경우_Remote_Address를_반환한다() {
        // given
        String remoteAddress = "1.2.3.4";
        String forwarded = "";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo(remoteAddress);
    }

    @Test
    void X_Forwarded_For_헤더에_IP_하나_있을경우_해당_IP를_반환한다() {
        // given
        String remoteAddress = "1.2.3.4";
        String forwarded = "3.4.5.6";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo(forwarded);
    }

    @ValueSource(strings = {
        "3.4.5.6 5.6.7.8",
        "3.4.5.6, 5.6.7.8",
        "3.4.5.6,5.6.7.8",
        "3.4.5.6, 5.6.7.8"
    })
    @ParameterizedTest
    void X_Forwarded_For_헤더에_여러_IP가_있는경우_첫번째_IP를_파싱해서_반환한다(String forwarded) {
        // given
        String remoteAddress = "1.2.3.4";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo("3.4.5.6");
    }

}
