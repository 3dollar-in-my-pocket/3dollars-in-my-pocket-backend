package com.depromeet.threedollar.domain.mongo.boss.domain.feedback

import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository.BossStoreFeedbackRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreFeedbackRepository : MongoRepository<BossStoreFeedback, String>, BossStoreFeedbackRepositoryCustom
