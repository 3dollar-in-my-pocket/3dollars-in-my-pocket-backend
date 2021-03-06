package com.depromeet.threedollar.batch.jobs.statistics.templates

enum class UserDailyStatisticsMessageFormat(
    val messageFormat: String,
) {

    DAILY_STATISTICS_INFO(
        """
            [가슴속 삼천원 %s 유저 서비스 통계 정보를 알려드립니다]
        """.trimIndent()
    ),
    COUNTS_USER(
        """
            1. 회원 통계 정보
            - 총 %s명이 가입하고 있습니다.
            - 오늘 %s명이 신규 가입하였습니다.
            - 일주일 간 %s명이 신규 가입하였습니다.
            - 한달 간 %s명이 신규 가입하였습니다.
        """.trimIndent()
    ),
    COUNTS_STORE(
        """
            2. 가게 통계 정보
            - 총 %s개의 가게가 등록되어 있습니다..
            - 오늘 %s개의 가게가 신규 등록되었습니다.
            - 일주일 간 %s개의 가게가 신규 등록되었습니다.
            - 한달 간 %s개의 가게가 신규 등록되었습니다.
        """.trimIndent()
    ),
    COUNTS_MENUS(
        """
            3. 활성화 중인 메뉴 정보
            %s
        """.trimIndent()
    ),
    COUNTS_MENU(
        """
            - %s: %s개가 활성화 되어 있습니다.
        """.trimIndent()
    ),
    COUNTS_STORE_IMAGE(
        """
            4. 가게 이미지 통계 정보
            - 총 %s개의 가게 이미지가 신규 등록되었습니다.
            - 오늘 %s개의 가게 이미지가 신규 등록되었습니다.
            - 일주일 간 %s개의 가게 이미지가 신규 등록되었습니다.
            - 한달 간 %s개의 가게 이미지가 신규 등록되었습니다.
        """.trimIndent()
    ),
    COUNTS_DELETED_STORE(
        """
            5. 가게 삭제 통계 정보
            - 총 %s개의 가게가 삭제 처리되었습니다.
            - 오늘 %s개의 가게가 삭제 처리되었습니다.
            - 일주일 간 %s개의 가게가 삭제 처리되었습니다.
            - 한달 간 %s개의 가게가 삭제 처리되었습니다.
        """.trimIndent()
    ),
    COUNTS_DELETE_STORE_REQUEST(
        """
            6. 가게 삭제 요청 통계 정보
            - 총 %s개의 가게 삭제 요청이 추가되었습니다.
            - 오늘 %s개의 가게 삭제 요청이 추가되었습니다.
            - 일주일 간 %s개의 가게 삭제 요청이 추가되었습니다.
            - 한달 간 %s개의 가게 삭제 요청이 추가되었습니다.
      """.trimIndent()
    ),
    COUNTS_REVIEW(
        """
            7. 리뷰 통계 정보
            - 총 %s개의 리뷰가 작성되어 있습니다.
            - 오늘 %s개의 리뷰가 신규 작성되었습니다.
            - 일주일 간 %s개의 리뷰가 신규 작성되었습니다.
            - 한달 간 %s개의 리뷰가 신규 작성되었습니다.
        """.trimIndent()
    ),
    COUNTS_VISIT_HISTORY(
        """
            8. 방문 인증 기록 통계 정보
            - 총 %s번의 방문 인증 기록이 등록되어 있습니다.
            - 오늘 %s번의 방문 인증 기록이 신규 등록되었습니다.
            - 일주일 간 %s번의 방문 인증 기록이 신규 등록되었습니다.
            - 한달 간 %s번의 방문 인증 기록이 신규 등록되었습니다.
        """.trimIndent()
    ),
    COUNTS_MEDALS(
        """
            9. 보유중인 메달 정보
            %s
        """.trimIndent()
    ),
    COUNTS_MEDAL(
        """
            - %s: 사용자 %s명이 보유하고 있습니다.
        """.trimIndent()
    ),
    COUNTS_ACTIVE_MEDALS(
        """
            10. 장착중인 메달 정보
            %s
        """.trimIndent()
    ),
    COUNTS_ACTIVE_MEDAL(
        """
            - %s: 사용자 %s명이 장착하고 있습니다.
        """.trimIndent()
    ),

}
