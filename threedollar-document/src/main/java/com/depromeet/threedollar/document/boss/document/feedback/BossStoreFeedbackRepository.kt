package com.depromeet.threedollar.document.boss.document.feedback

import org.springframework.data.mongodb.repository.MongoRepository

interface BossStoreFeedbackRepository : MongoRepository<BossStoreFeedback, String>
