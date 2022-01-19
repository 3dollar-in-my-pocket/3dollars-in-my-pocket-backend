package com.depromeet.threedollar.api.controller;

import com.depromeet.threedollar.common.utils.EnumMapper;
import com.depromeet.threedollar.common.model.EnumValue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class EnumMapperController {

    private final EnumMapper enumMapper;

    @GetMapping("/enums")
    public Map<String, List<EnumValue>> getEnum() {
        return enumMapper.getAll();
    }

}
