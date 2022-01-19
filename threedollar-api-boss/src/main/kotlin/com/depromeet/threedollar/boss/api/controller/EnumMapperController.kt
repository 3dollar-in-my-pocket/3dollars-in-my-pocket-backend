package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.common.model.EnumValue
import com.depromeet.threedollar.common.utils.EnumMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnumMapperController(
    private val enumMapper: EnumMapper
) {

    @GetMapping("/enums")
    fun getEnums(): Map<String, List<EnumValue>> {
        return enumMapper.all
    }

}
