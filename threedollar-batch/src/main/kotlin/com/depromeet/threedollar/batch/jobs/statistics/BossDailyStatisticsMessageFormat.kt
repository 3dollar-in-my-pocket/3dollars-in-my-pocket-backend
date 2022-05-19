package com.depromeet.threedollar.batch.jobs.statistics

enum class BossDailyStatisticsMessageFormat(
    val messageFormat: String,
) {

    DAILY_STATISTICS_INFO(
        """
            [가슴속 삼천원 %s 사장님 서비스 통계 정보를 알려드립니다]
        """.trimIndent()
    ),
    BOSS_ACCOUNT_STATISTICS(
        """
            1. 사장님 계정 통계 정보
            - 총 %s명이 가입하고 있습니다.
            - 오늘 %s명이 신규 가입하였습니다.
            - 일주일 간 %s명이 신규 가입하였습니다.
        """.trimIndent()
    ),
    BOSS_REGISTRATION_STATISTICS(
        """
            2. 사장님 계정 가입 신청 통계 정보
            - 총 %s명이 가입 신청하였습니다.
            - 오늘 %s명이 가입 신청하였습니다.
            - 일주일 간 %s명이 가입 신청하였습니다.
        """.trimIndent()
    ),
    BOSS_STORE_STATISTICS(
        """
            3. 사장님 가게 통계 정보
            - 총 %s개의 가게가 등록되어 있습니다..
            - 오늘 %s개의 가게가 신규 등록되었습니다.
            - 일주일 간 %s개의 가게가 신규 등록되었습니다.
        """.trimIndent()
    ),
    DELETED_BOSS_STORE_STATISTICS(
        """
            4. 사장님 가게 삭제 통계 정보
            - 총 %s개의 가게가 삭제 처리되었습니다.
            - 오늘 %s개의 가게가 삭제 처리되었습니다.
            - 일주일 간 %s개의 가게가 삭제 처리되었습니다.
        """.trimIndent()
    ),
    BOSS_STORE_FEEDBACK_STATISTICS(
        """
            5. 사장님 가게 피드백 통계 정보
            - 총 %s개의 피드백이 등록되어 있습니다..
            - 오늘 %s개의 피드백이 신규 등록되었습니다.
            - 일주일 간 %s개의 피드백이 신규 등록되었습니다.
        """.trimIndent()
    ),

}
