package com.depromeet.threedollar.api.userservice.config.interceptor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import com.depromeet.threedollar.api.userservice.ControllerTest;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.OsPlatformType;

class UserMetaInterceptorTest extends ControllerTest {

    @DisplayName("IPhone 1.0.0")
    @Test
    void IOS_디바이스_정보를_가져온다_1() throws Exception {
        // given
        String userAgent = "1.0.0 (com.macgongmon.-dollar-in-my-pocket; build:1; iOS 15.5.0)";
        String traceId = "Root=1-62b5d6a4-06a5fc494921c46949dc2355";

        // when & then
        mockMvc.perform(get("/test-device")
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header("X-Amzn-Trace-Id", traceId)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.traceId").value(traceId))
            .andExpect(jsonPath("$.data.osPlatform").value(OsPlatformType.IPHONE.name()))
            .andExpect(jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(jsonPath("$.data.applicationType").value(ApplicationType.USER_API.name()))
            .andExpect(jsonPath("$.data.appVersion").value("1.0.0"));
    }

    @DisplayName("IPhone 1.0.1")
    @Test
    void IOS_디바이스_정보를_가져온다_2() throws Exception {
        // given
        String userAgent = "1.0.1 (com.macgongmon.-dollar-in-my-pocket; build:1; iOS 15.5.0)";
        String traceId = "traceId";

        // when & then
        mockMvc.perform(get("/test-device")
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header("X-Amzn-Trace-Id", traceId)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.traceId").value(traceId))
            .andExpect(jsonPath("$.data.osPlatform").value(OsPlatformType.IPHONE.name()))
            .andExpect(jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(jsonPath("$.data.applicationType").value(ApplicationType.USER_API.name()))
            .andExpect(jsonPath("$.data.appVersion").value("1.0.1"));
    }

    @DisplayName("Android 1.0.0")
    @Test
    void AOS_디바이스_정보를_가져온다_1() throws Exception {
        // given
        String appVersion = "2.4.8";
        String userAgent = "okhttp/4.9.1";
        String traceId = "Root=1-62b5d6a4-06a5fc494921c46949dc2355";

        // when & then
        mockMvc.perform(get("/test-device")
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header("X-ANDROID-SERVICE-VERSION", appVersion)
                .header("X-Amzn-Trace-Id", traceId)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.traceId").value(traceId))
            .andExpect(jsonPath("$.data.osPlatform").value(OsPlatformType.ANDROID.name()))
            .andExpect(jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(jsonPath("$.data.applicationType").value(ApplicationType.USER_API.name()))
            .andExpect(jsonPath("$.data.appVersion").value(appVersion));
    }

    @DisplayName("Unknown Device")
    @Test
    void UNKNOWN_디바이스() throws Exception {
        // given
        String userAgent = "Unknown";
        String traceId = "Root=1-62b5d6a4-06a5fc494921c46949dc2355";

        // when & then
        mockMvc.perform(get("/test-device")
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header("X-Amzn-Trace-Id", traceId)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.traceId").value(traceId))
            .andExpect(jsonPath("$.data.osPlatform").value(OsPlatformType.UNKNOWN.name()))
            .andExpect(jsonPath("$.data.userAgent").value(userAgent))
            .andExpect(jsonPath("$.data.applicationType").value(ApplicationType.USER_API.name()))
            .andExpect(jsonPath("$.data.appVersion").isEmpty());
    }

}
