package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.common.utils.UuidUtils

class BossStoreMenu(
    val menuId: String = UuidUtils.generate(),
    val name: String,
    val price: Int,
    val imageUrl: String,
    val tag: String
)
