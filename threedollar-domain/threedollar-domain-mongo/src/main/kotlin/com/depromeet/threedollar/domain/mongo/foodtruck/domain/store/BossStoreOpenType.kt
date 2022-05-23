package com.depromeet.threedollar.domain.mongo.foodtruck.domain.store

import com.depromeet.threedollar.common.model.EnumModel

enum class BossStoreOpenType(
    private val description: String,
) : EnumModel {

    OPEN("영업중"),
    CLOSED("영업 종료"),
    ;

    override fun getKey(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

}

