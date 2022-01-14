package com.depromeet.threedollar.document.boss.document.store

import com.depromeet.threedollar.document.common.document.BaseDocument

class BossStoreMenu(
    val name: String,
    val price: Int,
    val menuCategory: String
) : BaseDocument()
