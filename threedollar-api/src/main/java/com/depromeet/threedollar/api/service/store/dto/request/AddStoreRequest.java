package com.depromeet.threedollar.api.service.store.dto.request;

import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoreRequest {

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @NotBlank(message = "{store.name.notBlank}")
    private String storeName;

    @Nullable
    private StoreType storeType;

    @NotNull(message = "{store.appearanceDays.notNull}")
    private Set<DayOfTheWeek> appearanceDays;

    @NotNull(message = "{store.paymentMethods.notNull}")
    private Set<PaymentMethodType> paymentMethods;

    @Valid
    @NotEmpty(message = "{store.menu.notEmpty}")
    private Set<MenuRequest> menus;

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public AddStoreRequest(Double latitude, Double longitude, String storeName, @Nullable StoreType storeType,
                           Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods, Set<MenuRequest> menus) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.storeType = storeType;
        this.appearanceDays = appearanceDays;
        this.paymentMethods = paymentMethods;
        this.menus = menus;
    }

    public Store toStore(Long userId) {
        Store store = Store.newInstance(userId, latitude, longitude, storeName, storeType);
        store.addPaymentMethods(paymentMethods);
        store.addAppearanceDays(appearanceDays);
        store.addMenus(this.menus.stream()
            .map(menu -> menu.toEntity(store))
            .collect(Collectors.toList())
        );
        return store;
    }

}
