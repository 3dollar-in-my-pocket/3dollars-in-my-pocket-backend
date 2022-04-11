package com.depromeet.threedollar.domain.rds.user.domain.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.utils.MathUtils;
import com.depromeet.threedollar.domain.rds.common.domain.AuditingTimeEntity;
import com.depromeet.threedollar.domain.rds.common.domain.Location;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    indexes = {
        @Index(name = "idx_store_1", columnList = "userId"),
        @Index(name = "idx_store_2", columnList = "latitude,longitude")
    }
)
public class Store extends AuditingTimeEntity {

    private static final String DELETE_STORE_NAME = "삭제된 가게입니다";
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PaymentMethod> paymentMethods = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppearanceDay> appearanceDays = new ArrayList<>();
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Menu> menus = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Embedded
    private Location location;
    @Column(nullable = false, length = 300)
    private String name;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private StoreType type;
    @Column(nullable = false)
    private double rating; // 평균 평가 점수
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Builder(access = AccessLevel.PACKAGE)
    private Store(Long userId, double latitude, double longitude, String name, @Nullable StoreType type, double rating) {
        this.userId = userId;
        this.location = Location.of(latitude, longitude);
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.status = StoreStatus.ACTIVE;
    }

    public static Store newInstance(Long userId, double latitude, double longitude, String storeName, @Nullable StoreType storeType) {
        return Store.builder()
            .userId(userId)
            .latitude(latitude)
            .longitude(longitude)
            .name(storeName)
            .type(storeType)
            .rating(0.0)
            .build();
    }

    public void addPaymentMethods(Set<PaymentMethodType> paymentMethodTypes) {
        for (PaymentMethodType paymentMethodType : paymentMethodTypes) {
            this.addPaymentMethod(paymentMethodType);
        }
    }

    private void addPaymentMethod(PaymentMethodType type) {
        this.paymentMethods.add(PaymentMethod.of(this, type));
    }

    public void updatePaymentMethods(Set<PaymentMethodType> paymentMethodTypes) {
        removePaymentMethodsNotIncludedInChanges(paymentMethodTypes);
        addNewPaymentMethods(paymentMethodTypes);
    }

    private void removePaymentMethodsNotIncludedInChanges(Set<PaymentMethodType> paymentMethodTypes) {
        this.paymentMethods.removeIf(paymentMethod -> !paymentMethodTypes.contains(paymentMethod.getMethod()));
    }

    private void addNewPaymentMethods(Set<PaymentMethodType> paymentMethodTypes) {
        Set<PaymentMethodType> currentPaymentMethodTypes = getPaymentMethodTypes();
        addPaymentMethods(paymentMethodTypes.stream()
            .filter(paymentMethodType -> !currentPaymentMethodTypes.contains(paymentMethodType))
            .collect(Collectors.toSet()));
    }

    public Set<PaymentMethodType> getPaymentMethodTypes() {
        return this.paymentMethods.stream()
            .map(PaymentMethod::getMethod)
            .collect(Collectors.toSet());
    }

    public void addAppearanceDays(Set<DayOfTheWeek> dayOfTheWeeks) {
        for (DayOfTheWeek dayOfTheWeek : dayOfTheWeeks) {
            addAppearanceDay(dayOfTheWeek);
        }
    }

    private void addAppearanceDay(DayOfTheWeek dayOfTheWeek) {
        this.appearanceDays.add(AppearanceDay.of(this, dayOfTheWeek));
    }

    public void updateAppearanceDays(Set<DayOfTheWeek> dayOfTheWeeks) {
        removeAppearanceDaysNotIncludedInChanges(dayOfTheWeeks);
        addNewAppearanceDays(dayOfTheWeeks);
    }

    private void removeAppearanceDaysNotIncludedInChanges(Set<DayOfTheWeek> dayOfTheWeeks) {
        this.appearanceDays.removeIf(appearanceDay -> !dayOfTheWeeks.contains(appearanceDay.getDay()));
    }

    private void addNewAppearanceDays(Set<DayOfTheWeek> dayOfTheWeeks) {
        Set<DayOfTheWeek> hasDayOfTheWeek = getAppearanceDayTypes();
        addAppearanceDays(dayOfTheWeeks.stream()
            .filter(day -> !hasDayOfTheWeek.contains(day))
            .collect(Collectors.toSet()));
    }

    public Set<DayOfTheWeek> getAppearanceDayTypes() {
        return this.appearanceDays.stream()
            .map(AppearanceDay::getDay)
            .collect(Collectors.toSet());
    }

    public void addMenus(List<Menu> menus) {
        for (Menu menu : menus) {
            this.addMenu(menu);
        }
    }

    private void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public void updateMenu(List<Menu> menus) {
        removeMenusNotIncludedInChanges(menus);
        addNewMenus(menus);
    }

    private void removeMenusNotIncludedInChanges(List<Menu> menus) {
        this.menus.removeIf(menu -> !menus.contains(menu));
    }

    private void addNewMenus(List<Menu> menus) {
        this.menus.addAll(menus.stream()
            .filter(newMenu -> !this.menus.contains(newMenu))
            .collect(Collectors.toList()));
    }

    public void updateInfo(String name, StoreType type, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.location = Location.of(latitude, longitude);
    }

    public void updateAverageRating(double average) {
        this.rating = MathUtils.round(average, 1);
    }

    public void delete() {
        this.status = StoreStatus.DELETED;
    }

    public double getLatitude() {
        return this.location.getLatitude();
    }

    public double getLongitude() {
        return this.location.getLongitude();
    }

    public List<MenuCategoryType> getMenuCategoriesSortedByCounts() {
        Map<MenuCategoryType, Long> counts = getCurrentMenuCategoryGroupByCounts();
        return counts.entrySet().stream()
            .sorted(Map.Entry.<MenuCategoryType, Long>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private Map<MenuCategoryType, Long> getCurrentMenuCategoryGroupByCounts() {
        return this.menus.stream()
            .collect(Collectors.groupingBy(Menu::getCategory, Collectors.counting()));
    }

    public boolean hasMenuCategory(MenuCategoryType category) {
        return this.menus.stream()
            .anyMatch(menu -> menu.isCategory(category));
    }

    public double getRating() {
        return MathUtils.round(rating, 1);
    }

    @NotNull
    public String getName() {
        if (isDeleted()) {
            return DELETE_STORE_NAME;
        }
        return this.name;
    }

    public boolean isDeleted() {
        return StoreStatus.DELETED.equals(this.status);
    }

}
