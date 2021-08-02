package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.domain.domain.common.AuditingTimeEntity;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.common.Location;
import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_store_1", columnList = "userId"),
        @Index(name = "idx_store_2", columnList = "status"),
        @Index(name = "idx_store_3", columnList = "id,latitude,longitude")
    }
)
public class Store extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Embedded
    private Location location;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StoreType type;

    @Column(nullable = false)
    private double rating; // 평균 평가 점수

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PaymentMethod> paymentMethods = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppearanceDay> appearanceDays = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Menu> menus = new ArrayList<>();

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder
    private Store(Long userId, double latitude, double longitude, String name, StoreType type) {
        this.userId = userId;
        this.location = Location.of(latitude, longitude);
        this.name = name;
        this.type = type;
        this.rating = 0.0;
        this.status = StoreStatus.ACTIVE;
    }

    public static Store newInstance(Long userId, double latitude, double longitude, String storeName, StoreType storeType) {
        return new Store(userId, latitude, longitude, storeName, storeType);
    }

    private void addPaymentMethod(PaymentMethodType type) {
        PaymentMethod paymentMethod = PaymentMethod.of(this, type);
        this.paymentMethods.add(paymentMethod);
    }

    public void addPaymentMethods(Set<PaymentMethodType> types) {
        for (PaymentMethodType type : types) {
            this.addPaymentMethod(type);
        }
    }

    public void updatePaymentMethods(Set<PaymentMethodType> paymentMethodTypes) {
        this.paymentMethods.removeIf(paymentMethod -> !paymentMethodTypes.contains(paymentMethod.getMethod()));

        Set<PaymentMethodType> hasPaymentTypes = getPaymentMethodTypes();
        addPaymentMethods(paymentMethodTypes.stream()
            .filter(type -> !hasPaymentTypes.contains(type))
            .collect(Collectors.toSet()));
    }

    private Set<PaymentMethodType> getPaymentMethodTypes() {
        return this.paymentMethods.stream()
            .map(PaymentMethod::getMethod)
            .collect(Collectors.toSet());
    }

    private void addAppearanceDay(DayOfTheWeek dayOfTheWeek) {
        AppearanceDay appearanceDay = AppearanceDay.of(this, dayOfTheWeek);
        this.appearanceDays.add(appearanceDay);
    }

    public void addAppearanceDays(Set<DayOfTheWeek> dayOfTheWeeks) {
        for (DayOfTheWeek dayOfTheWeek : dayOfTheWeeks) {
            addAppearanceDay(dayOfTheWeek);
        }
    }

    public void updateAppearanceDays(Set<DayOfTheWeek> dayOfTheWeeks) {
        this.appearanceDays.removeIf(appearanceDay -> !dayOfTheWeeks.contains(appearanceDay.getDay()));

        Set<DayOfTheWeek> hasDayOfTheWeek = getDayOfTheWeek();
        addAppearanceDays(dayOfTheWeeks.stream()
            .filter(day -> !hasDayOfTheWeek.contains(day))
            .collect(Collectors.toSet()));
    }

    private Set<DayOfTheWeek> getDayOfTheWeek() {
        return this.appearanceDays.stream()
            .map(AppearanceDay::getDay)
            .collect(Collectors.toSet());
    }

    private void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public void addMenus(List<Menu> menus) {
        for (Menu menu : menus) {
            this.addMenu(menu);
        }
    }

    public void updateMenu(List<Menu> menus) {
        this.menus.clear();
        addMenus(menus);
    }

    public void updateLocation(Double latitude, Double longitude) {
        this.location = Location.of(latitude, longitude);
    }

    public void updateInfo(String name, StoreType type, Long userId) {
        this.name = name;
        this.type = type;
        this.userId = userId;
    }

    public Double getLatitude() {
        return this.location.getLatitude();
    }

    public Double getLongitude() {
        return this.location.getLongitude();
    }

    public void delete() {
        this.status = StoreStatus.DELETED;
    }

    public MenuCategoryType getRepresentativeCategory() {
        if (this.menus.isEmpty()) {
            return null;
        }
        Map<MenuCategoryType, Long> menusCounts = this.menus.stream()
            .collect(Collectors.groupingBy(Menu::getCategory, Collectors.counting()));
        return Collections.max(menusCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public List<MenuCategoryType> getMenuCategories() {
        return this.menus.stream()
            .map(Menu::getCategory).distinct()
            .collect(Collectors.toList());
    }

    public Set<DayOfTheWeek> getAppearanceDaysType() {
        return this.appearanceDays.stream()
            .map(AppearanceDay::getDay)
            .collect(Collectors.toSet());
    }

    public Set<PaymentMethodType> getPaymentMethodsType() {
        return this.paymentMethods.stream()
            .map(PaymentMethod::getMethod)
            .collect(Collectors.toSet());
    }

    public void updateAverageRating(double average) {
        this.rating = average;
    }

}
