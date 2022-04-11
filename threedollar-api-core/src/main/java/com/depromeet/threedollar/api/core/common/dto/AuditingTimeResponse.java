package com.depromeet.threedollar.api.core.common.dto;

import java.time.LocalDateTime;

import com.depromeet.threedollar.domain.mongo.common.domain.BaseDocument;
import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
