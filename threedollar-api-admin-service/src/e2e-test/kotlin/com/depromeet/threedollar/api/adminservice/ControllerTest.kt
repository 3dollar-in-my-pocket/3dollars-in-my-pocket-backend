package com.depromeet.threedollar.api.adminservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import com.fasterxml.jackson.databind.ObjectMapper

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal abstract class ControllerTest : IntegrationTest() {

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

}
