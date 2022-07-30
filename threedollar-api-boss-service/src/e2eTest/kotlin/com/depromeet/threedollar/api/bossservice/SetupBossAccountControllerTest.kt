package com.depromeet.threedollar.api.bossservice

import com.depromeet.threedollar.api.bossservice.service.auth.dto.response.LoginResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

internal abstract class SetupBossAccountControllerTest : SetupControllerTest() {

    @Autowired
    protected lateinit var bossAccountRepository: BossAccountRepository

    protected lateinit var token: String

    protected lateinit var bossId: String

    @BeforeEach
    protected fun setupToken() {
        val response = objectMapper.readValue(
            mockMvc.perform(get("/test-token"))
                .andReturn()
                .response
                .contentAsString, object : TypeReference<ApiResponse<LoginResponse>>() {}
        )
        token = response.data.token
        bossId = response.data.bossId
    }

    override fun cleanup() {
        super.cleanup()
        bossAccountRepository.deleteAll()
    }

}
