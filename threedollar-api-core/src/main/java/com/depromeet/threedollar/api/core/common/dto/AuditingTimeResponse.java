package com.depromeet.threedollar.api.core.common.dto;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
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

    protected void setBaseTime(AuditingTimeEntity auditingTimeEntity) {
        this.createdAt = auditingTimeEntity.getCreatedAt();
        this.updatedAt = auditingTimeEntity.getUpdatedAt();
    }

    protected void setBaseTime(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
