package com.depromeet.threedollar.document.boss.document.registration.repository

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.registration.Registration

interface RegistrationRepositoryCustom {

    fun existsRegistrationBySocialInfo(socialId: String, socialType: BossAccountSocialType): Boolean

}
