package com.depromeet.threedollar.domain.user.domain.store;

import com.depromeet.threedollar.domain.common.domain.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private PaymentMethod(Store store, PaymentMethodType method) {
        this.store = store;
        this.method = method;
    }

    static PaymentMethod of(Store store, PaymentMethodType type) {
        return new PaymentMethod(store, type);
    }

}
