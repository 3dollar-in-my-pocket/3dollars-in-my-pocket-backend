package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.model.EnumModel;
import com.depromeet.threedollar.common.model.EnumValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumMapper {

    private final Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e) {
        return Arrays.stream(e.getEnumConstants())
            .map(EnumValue::of)
            .collect(Collectors.toList());
    }

    public void put(String key, Class<? extends EnumModel> e) {
        factory.put(key, toEnumValues(e));
    }

    public Map<String, List<EnumValue>> getAll() {
        return factory;
    }

}
