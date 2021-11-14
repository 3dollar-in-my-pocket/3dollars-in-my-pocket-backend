package com.depromeet.threedollar.domain.domain.store;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentMethodType {

    CASH("현금"),
    ACCOUNT_TRANSFER("계좌이체"),
    CARD("카드");

    private final String paymentMethodType;

}
