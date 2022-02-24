package com.depromeet.threedollar.document.boss.document.feedback

import com.depromeet.threedollar.document.boss.document.feedback.repository.BossStoreFeedbackRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreFeedbackRepository : MongoRepository<BossStoreFeedback, String>, BossStoreFeedbackRepositoryCustom
