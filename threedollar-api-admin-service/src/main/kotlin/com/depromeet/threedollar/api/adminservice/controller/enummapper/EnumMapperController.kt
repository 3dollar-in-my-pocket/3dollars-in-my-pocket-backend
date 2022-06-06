package com.depromeet.threedollar.api.adminservice.controller.enummapper

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.model.EnumValue
import com.depromeet.threedollar.common.utils.EnumMapper
import io.swagger.annotations.ApiOperation

@RestController
class EnumMapperController(
    private val enumMapper: EnumMapper,
) {

    @ApiOperation("클라이언트에서 사용되는 Enum 목록을 조회합니다.")
    @GetMapping("/v1/enums")
    fun getEnums(): ApiResponse<Map<String, List<EnumValue>>> {
        return ApiResponse.success(enumMapper.all)
    }

}
