package com.depromeet.threedollar.domain.domain.store;

import com.depromeet.threedollar.common.utils.MathUtils;
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
        @Index(name = "idx_store_1", columnList = "userId,status"),
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

    @Column(length = 30)
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

    @Builder(access = AccessLevel.PACKAGE)
    private Store(Long userId, double latitude, double longitude, String name, StoreType type, double rating) {
        this.userId = userId;
        this.location = Location.of(latitude, longitude);
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.status = StoreStatus.ACTIVE;
    }

    public static Store newInstance(Long userId, double latitude, double longitude, String storeName, StoreType storeType) {
        return Store.builder()
            .userId(userId)
            .latitude(latitude)
            .longitude(longitude)
            .name(storeName)
            .type(storeType)
            .rating(0.0)
            .build();
    }

    public void addPaymentMethods(Set<PaymentMethodType> types) {
        for (PaymentMethodType type : types) {
            this.addPaymentMethod(type);
        }
    }

    private void addPaymentMethod(PaymentMethodType type) {
        PaymentMethod paymentMethod = PaymentMethod.of(this, type);
        this.paymentMethods.add(paymentMethod);
    }

    public void updatePaymentMethods(Set<PaymentMethodType> paymentMethodTypes) {
        this.paymentMethods.removeIf(paymentMethod -> !paymentMethodTypes.contains(paymentMethod.getMethod()));

        Set<PaymentMethodType> hasPaymentTypes = getPaymentMethodTypes();
        addPaymentMethods(paymentMethodTypes.stream()
            .filter(type -> !hasPaymentTypes.contains(type))
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
        this.appearanceDays.removeIf(appearanceDay -> !dayOfTheWeeks.contains(appearanceDay.getDay()));

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
        this.menus.clear();
        addMenus(menus);
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
        if (this.menus.isEmpty()) {
            return Collections.emptyList();
        }
        Map<MenuCategoryType, Long> counts = getMenuCountsMap();
        return counts.entrySet().stream()
            .sorted(Map.Entry.<MenuCategoryType, Long>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private Map<MenuCategoryType, Long> getMenuCountsMap() {
        return this.menus.stream()
            .collect(Collectors.groupingBy(Menu::getCategory, Collectors.counting()));
    }

    public boolean hasMenuCategory(MenuCategoryType category) {
        return this.getMenuCategoryTypes().contains(category);
    }

    private List<MenuCategoryType> getMenuCategoryTypes() {
        return this.menus.stream()
            .map(Menu::getCategory)
            .collect(Collectors.toList());
    }

    public double getRating() {
        return MathUtils.round(rating, 1);
    }

}
