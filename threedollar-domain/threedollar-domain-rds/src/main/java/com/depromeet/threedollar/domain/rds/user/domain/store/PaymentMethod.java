package com.depromeet.threedollar.domain.rds.user.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaymentMethod extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private PaymentMethodType method;

    private PaymentMethod(@NotNull Store store, @NotNull PaymentMethodType method) {
        this.store = store;
        this.method = method;
    }

    static PaymentMethod of(@NotNull Store store, @NotNull PaymentMethodType type) {
        return new PaymentMethod(store, type);
    }

}
