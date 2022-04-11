package com.depromeet.threedollar.domain.mongo.boss.domain.feedback

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository.BossStoreFeedbackRepositoryCustom
import com.depromeet.threedollar.domain.mongo.boss.domain.feedback.repository.BossStoreFeedbackStatisticsRepositoryCustom

interface BossStoreFeedbackRepository : MongoRepository<BossStoreFeedback, String>, BossStoreFeedbackRepositoryCustom, BossStoreFeedbackStatisticsRepositoryCustom
