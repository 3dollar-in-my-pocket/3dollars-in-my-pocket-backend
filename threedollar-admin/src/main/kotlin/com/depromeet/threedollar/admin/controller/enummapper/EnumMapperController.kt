package com.depromeet.threedollar.admin.controller.enummapper

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.model.EnumValue
import com.depromeet.threedollar.common.utils.EnumMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnumMapperController(
    private val enumMapper: EnumMapper
) {

    @GetMapping("/enums")
    fun getEnums(): ApiResponse<Map<String, List<EnumValue>>> {
        return ApiResponse.success(enumMapper.all)
    }

}
