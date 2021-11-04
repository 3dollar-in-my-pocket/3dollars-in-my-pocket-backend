package com.depromeet.threedollar.batch.jobs.statistics

enum class StaticsMessageType(
    val messageFormat: String
) {

    COUNTS_USER(
        "[가슴속 삼천원 %s 통계 정보를 알려드립니다]\n" +
            "1. 회원 가입 수\n" +
            "- 총 %s명이 가입하고 있습니다\n" +
            "- 금일 %s명이 신규 가입하였습니다\n" +
            "- 금주 %s명이 신규 가입하였습니다"
    ),
    COUNTS_STORE(
        "2. 활성 가게 수\n" +
            "- 총 %s개의 가게가 활성화 되어 있습니다\n" +
            "- 금일 %s개의 가게가 신규 등록되었습니다.\n" +
            "- 금주 %s개의 가게가 신규 등록되었습니다.\n" +
            "- 금일 %s개의 가게가 삭제되었습니다"
    ),
    COUNTS_REVIEW(
        "4. 활성 리뷰 수\n" +
            "- 총 %s개의 리뷰를 작성해주셨습니다.\n" +
            "- 금일 %s개의 리뷰를 신규 작성해주셨습니다.\n" +
            "- 금주 %s개의 리뷰를 신규 작성해주셨습니다."
    ),
    COUNTS_MENU(
        "- %s: %s개가 활성화 되어 있습니다"
    ),
    COUNTS_MENUS(
        "3. 활성화된 메뉴 정보\n%s"
    )

}
