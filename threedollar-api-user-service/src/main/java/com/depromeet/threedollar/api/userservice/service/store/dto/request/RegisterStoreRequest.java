package com.depromeet.threedollar.api.userservice.service.store.dto.request;

import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterStoreRequest {

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @Size(max = 150, message = "{store.name.size}")
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

    @Builder(builderMethodName = "testBuilder")
    public RegisterStoreRequest(Double latitude, Double longitude, String storeName, @Nullable StoreType storeType,
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
