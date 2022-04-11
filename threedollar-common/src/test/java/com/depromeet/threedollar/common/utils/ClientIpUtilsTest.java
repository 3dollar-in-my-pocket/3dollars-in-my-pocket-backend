package com.depromeet.threedollar.common.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.depromeet.threedollar.common.exception.model.InternalServerException;

class ClientIpUtilsTest {

    @Test
    void PublicIP인경우_RemoteAddress를_반환한다() {
        // given
        String remoteAddress = "1.2.3.4";
        String forwarded = "3.4.5.6";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo(remoteAddress);
    }

    @ValueSource(strings = {
        "3.4.5.6",
        "3.4.5.6 5.6.7.8",
        "3.4.5.6, 5.6.7.8",
        "3.4.5.6,5.6.7.8",
        "3.4.5.6 5.6.7.8 11.12.13.14"
    })
    @ParameterizedTest
    void 사설IP인경우_X_Forwarded_For_헤더에_여러_IP가_있는경우_첫번째_IP를_파싱해서_반환한다(String forwarded) {
        // given
        String remoteAddress = "10.0.0.1";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo("3.4.5.6");
    }

    @ValueSource(strings = {
        "10.0.0.1",
        "172.16.0.1",
        "192.168.0.1"
    })
    @ParameterizedTest
    void RemoteAddress가_PrivateIP인경우_X_forwarded_for_헤더에서_추출한IP를_반환한다(String remoteAddress) {
        // given
        String forwarded = "1.2.3.4";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo(forwarded);
    }

    @Test
    void RemoteAddress가_사설IP이지만_X_Forwarded_For_헤더가_null인경우_Remote_Address를_반환한다() {
        // given
        String remoteAddress = "10.0.0.1";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, null);

        // then
        assertThat(clientIp).isEqualTo(remoteAddress);
    }

    @Test
    void RemoteAddress가_사설IP이지만_X_Forwarded_For_헤더가_빈경우_Remote_Address를_반환한다() {
        // given
        String remoteAddress = "1.2.3.4";
        String forwarded = "";

        // when
        String clientIp = ClientIpUtils.getClientIp(remoteAddress, forwarded);

        // then
        assertThat(clientIp).isEqualTo(remoteAddress);
    }

    @Test
    void 잘못된_IP포맷인경우_throw_InternalServerException() {
        String remoteAddress = "wrong-ip";
        String forwarded = "";

        // when & then
        assertThatThrownBy(() -> ClientIpUtils.getClientIp(remoteAddress, forwarded)).isInstanceOf(InternalServerException.class);
    }

}
