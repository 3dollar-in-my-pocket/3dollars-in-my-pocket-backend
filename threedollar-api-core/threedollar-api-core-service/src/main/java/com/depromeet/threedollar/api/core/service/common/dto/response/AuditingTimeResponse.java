package com.depromeet.threedollar.api.core.service.common.dto.response;

import com.depromeet.threedollar.domain.mongo.core.model.BaseDocument;
import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AuditingTimeResponse {

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;

    protected void setAuditingTimeByEntity(AuditingTimeEntity auditingTimeEntity) {
        this.createdAt = auditingTimeEntity.getCreatedAt();
        this.updatedAt = auditingTimeEntity.getUpdatedAt();
    }

    protected void setAuditingTimeByDocument(BaseDocument baseTime) {
        this.createdAt = baseTime.createdAt;
        this.updatedAt = baseTime.updatedAt;
    }

    protected void setAuditingTime(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
