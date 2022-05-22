package com.depromeet.threedollar.domain.rds.user.domain.store;

import com.depromeet.threedollar.common.model.EnumModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentMethodType implements EnumModel {

    CASH("현금"),
    ACCOUNT_TRANSFER("계좌이체"),
    CARD("카드");

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

}
