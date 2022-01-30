package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.utils.EnumMapper;
import com.depromeet.threedollar.common.model.EnumValue;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class EnumMapperController {

    private final EnumMapper enumMapper;

    @ApiOperation("클라이언트에서 사용되는 Enum 목록을 조회합니다.")
    @GetMapping("/enums")
    public ApiResponse<Map<String, List<EnumValue>>> getEnum() {
        return ApiResponse.success(enumMapper.getAll());
    }

}
