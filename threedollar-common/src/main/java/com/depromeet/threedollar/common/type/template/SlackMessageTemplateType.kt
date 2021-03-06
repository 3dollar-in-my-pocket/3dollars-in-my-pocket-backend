package com.depromeet.threedollar.common.type.template

/**
 * 슬랙 알림 메시지 템플릿
 */
enum class SlackMessageTemplateType(
    private val template: String,
) {

    ERROR_MESSAGE("""
    %s에서 에러가 발생하였습니다

    [에러 코드]
    %s

    [요청 URI]
    %s

    [에러 내용]
    %s

    [에러 발생시간]
    %s

    [사용자 에러 메시지]
    %s

    [유저 메타 정보]
    %s
    -------------------------------------
    """.trimIndent()
    ),

    INFO_MESSAGE("""
    %s에서 발생한 이벤트 정보를 알려드립니다
    [애플리케이션 id]
    %s

    [이벤트 내용]
    %s

    [발생 시간]
    %s
    -------------------------------------
    """.trimIndent()
    ),

    NEW_BOSS_REGISTRATION_MESSAGE(
        """
    새로운 사장님이 가입 신청하였습니다. (%s)

    [성함]
    %s

    [가입 소셜 플랫폼]
    %s

    [사업자번호]
    %s

    [가게이름]
    %s

    [카테고리]
    %s

    [연락처]:
    %s

    [가게 인증사진]
    %s
    -------------------------------------
    """.trimIndent(),
    ),

    BOSS_REGISTRATION_APPROVED_MESSAGE("""
    사장님 가입 신청이 승인되었습니다. (%s)
    -------------------------------------
    """.trimIndent()
    ),
    BOSS_REGISTRATION_DENIED_MESSAGE("""
    사장님 가입 신청이 반려되었습니다. (%s)
    -------------------------------------
    """.trimIndent()
    ),
    BATCH_FAILED_MESSAGE("""
    (%s) 배치 잡이 정상 처리되지 못했습니다. status: (%s)
    -------------------------------------
    """.trimIndent()
    )
    ;

    fun generateMessage(vararg args: Any?): String {
        return String.format(template, *args)
    }

}
