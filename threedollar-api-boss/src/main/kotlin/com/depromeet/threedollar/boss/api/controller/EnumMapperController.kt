package com.depromeet.threedollar.boss.api.controller

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.common.model.EnumValue
import com.depromeet.threedollar.common.utils.EnumMapper
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnumMapperController(
    private val enumMapper: EnumMapper
) {

    @ApiOperation("클라이언트에서 사용되는 Enum 목록을 조회합니다.")
    @GetMapping("/boss/enums")
    fun getEnums(): ApiResponse<Map<String, List<EnumValue>>> {
        return ApiResponse.success(enumMapper.all)
    }

}
