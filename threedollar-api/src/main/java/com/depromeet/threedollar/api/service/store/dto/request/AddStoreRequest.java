package com.depromeet.threedollar.api.service.store.dto.request;

import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoreRequest {

    @NotNull(message = "{store.latitude.notnull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notnull}")
    private Double longitude;

    @NotBlank(message = "{store.name.notBlank}")
    private String storeName;

    @NotNull(message = "{store.type.notnull}")
    private StoreType storeType;

    @NotNull(message = "{store.appearanceDays.notnull}")
    private Set<DayOfTheWeek> appearanceDays = new HashSet<>();

    @NotNull(message = "{store.paymentMethods.notnull}")
    private Set<PaymentMethodType> paymentMethods = new HashSet<>();

    @NotNull(message = "{store.menu.notnull}")
    private List<MenuRequest> menu = new ArrayList<>();

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public AddStoreRequest(Double latitude, Double longitude, String storeName, StoreType storeType,
                           Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods, List<MenuRequest> menu) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.storeType = storeType;
        this.appearanceDays = appearanceDays;
        this.paymentMethods = paymentMethods;
        this.menu = menu;
    }

    public Store toStore(Long userId) {
        Store store = Store.newInstance(userId, latitude, longitude, storeName, storeType);
        store.addPaymentMethods(paymentMethods);
        store.addAppearanceDays(appearanceDays);
        store.addMenus(toMenus(store));
        return store;
    }

    private List<Menu> toMenus(Store store) {
        return menu.stream()
            .map(menu -> menu.toEntity(store))
            .collect(Collectors.toList());
    }

}
