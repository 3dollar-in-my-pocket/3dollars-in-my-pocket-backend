package com.depromeet.threedollar.domain.rds.domain.commonservice.faq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.domain.rds.core.model.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_faq_1", columnList = "category")
    }
)
public class Faq extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private FaqCategory category;

    @Column(nullable = false, length = 100)
    private String question;

    @Column(nullable = false, length = 200)
    private String answer;

    @Builder(access = AccessLevel.PACKAGE)
    private Faq(@NotNull ApplicationType applicationType, @NotNull FaqCategory category, @NotNull String question, @NotNull String answer) {
        this.applicationType = applicationType;
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public static Faq newInstance(@NotNull ApplicationType applicationType, @NotNull FaqCategory category, @NotNull String question, @NotNull String answer) {
        return Faq.builder()
            .applicationType(applicationType)
            .category(category)
            .question(question)
            .answer(answer)
            .build();
    }

    public void update(@NotNull String question, @NotNull String answer, @NotNull FaqCategory category) {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

}
