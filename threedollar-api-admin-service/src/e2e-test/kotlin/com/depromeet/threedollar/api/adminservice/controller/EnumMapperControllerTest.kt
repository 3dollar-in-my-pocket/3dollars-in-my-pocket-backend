package com.depromeet.threedollar.api.adminservice.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.adminservice.ControllerTest

internal class EnumMapperControllerTest : ControllerTest() {

    @DisplayName("GET /admin/v1/enums")
    @Test
    fun `클라이언트에서 사용되는 Enum 목록을 반환한다`() {
        // when
        mockMvc.get("/v1/enums")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

}
