package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.common.Location;
import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.menu.MenuCreator;
import com.depromeet.threedollar.domain.domain.menu.MenuRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequest;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StoreServiceTest extends SetupUserServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @Autowired
    private AppearanceDayRepository appearanceDayRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        appearanceDayRepository.deleteAllInBatch();
        paymentMethodRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        storeDeleteRequestRepository.deleteAll();
    }

    @Nested
    class 가게_정보_등록 {

        @AutoSource
        @ParameterizedTest
        void 가게_정보_등록_성공시_새로운_가게_데이터가_DB에_추가된다(String storeName, StoreType storeType) {
            // given
            double latitude = 34.0;
            double longitude = 130.0;

            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("메뉴 이름", "한 개에 만원", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeService.addStore(request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertStore(stores.get(0), latitude, longitude, storeName, storeType, userId);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_정보_등록_성공시_게시일_테이블에_새로운_게시일_정보도_추가된다(Set<DayOfTheWeek> appearanceDays) {
            // given
            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(appearanceDays)
                .paymentMethods(Collections.emptySet())
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.addStore(request, userId);

            // then
            List<AppearanceDay> appearanceDayList = appearanceDayRepository.findAll();
            assertThat(appearanceDayList).hasSize(appearanceDays.size());
            assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_정보_등록_성공시_결제방법_테이블에_결제_방법도_추가된다(Set<PaymentMethodType> paymentMethods) {
            // given
            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(paymentMethods)
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.addStore(request, userId);

            // then
            List<PaymentMethod> paymentMethodsList = paymentMethodRepository.findAll();
            assertThat(paymentMethodsList).hasSize(paymentMethods.size());
            assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethods);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_정보_등록_성공시_메뉴_테이블에_메뉴들도_함께_추가된다(String menuName, String price, MenuCategoryType type) {
            // given
            Set<MenuRequest> menus = Set.of(MenuRequest.of(menuName, price, type));

            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.addStore(request, userId);

            // then
            List<Menu> menuList = menuRepository.findAll();
            assertThat(menuList).hasSize(1);
            assertMenu(menuList.get(0), menuName, price, type);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_추가시_중복된_메뉴는_한개만_저장된다(String menuName, String price, MenuCategoryType type) {
            // given
            Set<MenuRequest> menus = new HashSet<>(List.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(menuName, price, type))
            );

            AddStoreRequest request = AddStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.addStore(request, userId);

            // then
            List<Menu> menuList = menuRepository.findAll();
            assertThat(menuList).hasSize(1);
            assertMenu(menuList.get(0), menuName, price, type);
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 가게_정보_수정 {

        @AutoSource
        @ParameterizedTest
        void 가게에_대한_정보를_수정한다(String storeName, StoreType storeType, Set<DayOfTheWeek> appearanceDays, Set<PaymentMethodType> paymentMethods) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(appearanceDays)
                .paymentMethods(paymentMethods)
                .menus(Set.of(MenuRequest.of("메뉴 이름", "가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertStore(stores.get(0), latitude, longitude, storeName, storeType, userId);

            List<AppearanceDay> appearanceDayList = appearanceDayRepository.findAll();
            assertThat(appearanceDayList).hasSize(appearanceDays.size());
            assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays);

            List<PaymentMethod> paymentMethodsList = paymentMethodRepository.findAll();
            assertThat(paymentMethodsList).hasSize(paymentMethods.size());
            assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethods);
        }

        @AutoSource
        @ParameterizedTest
        void 가게의_메뉴정보를_수정한다(String menuName, String price, MenuCategoryType type) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;

            Set<MenuRequest> menu = Set.of(MenuRequest.of(menuName, price, type));

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName("가게 이름")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(menu)
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);

            List<Menu> menus = menuRepository.findAll();
            assertThat(menus).hasSize(1);
            assertMenu(menus.get(0), menuName, price, type);
        }

        @AutoSource
        @ParameterizedTest
        void 가게의_결제방법을_수정한다(Set<PaymentMethodType> paymentMethodTypes) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            store.addPaymentMethods(Set.of(PaymentMethodType.CARD));
            storeRepository.save(store);

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(paymentMethodTypes)
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<PaymentMethod> paymentMethodsList = paymentMethodRepository.findAll();
            assertThat(paymentMethodsList).hasSize(paymentMethodTypes.size());
            assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethodTypes);
        }

        @AutoSource
        @ParameterizedTest
        void 가게의_개시일을_수정한다(Set<DayOfTheWeek> appearanceDays) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            store.addAppearanceDays(Set.of(DayOfTheWeek.TUESDAY, DayOfTheWeek.WEDNESDAY));
            storeRepository.save(store);

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(appearanceDays)
                .paymentMethods(Collections.emptySet())
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<AppearanceDay> appearanceDayList = appearanceDayRepository.findAll();
            assertThat(appearanceDayList).hasSize(appearanceDays.size());
            assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays);
        }

        @AutoSource
        @ParameterizedTest
        void 가게_메뉴_수정시_중복된_메뉴는_한개만_저장된다(String menuName, String price, MenuCategoryType type) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(Menu.of(store, "이름", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            Set<MenuRequest> menus = new HashSet<>(List.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(menuName, price, type))
            );

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Menu> findMenus = menuRepository.findAll();
            assertThat(findMenus).hasSize(1);
            assertMenu(findMenus.get(0), store.getId(), menuName, price, type);
        }

        @Test
        void 가게_수정시_해당하는_가게가_존재하지_않으면_NOT_FOUND_STORE_EXCEPTION() {
            // given
            Long notFoundStoreId = -1L;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.TUESDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("메뉴 이름", "메뉴 가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when & then
            assertThatThrownBy(() -> storeService.updateStore(notFoundStoreId, request)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void 사용자가_작성하지_않은_가게_정보도_수정할수있다_단_최초제보자가_가게_제보자로_유지된다() {
            // given
            Long creatorUserId = 100L;

            Store store = StoreCreator.create(creatorUserId, "storeName");
            store.addMenus(List.of(MenuCreator.create(store, "붕어빵", "만원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;
            String storeName = "붕어빵";
            StoreType storeType = StoreType.STORE;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("메뉴 이름", "가격", MenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertStore(stores.get(0), latitude, longitude, storeName, storeType, creatorUserId);
        }

    }

    @Nested
    class 가게_삭제_요청 {

        @EnumSource
        @ParameterizedTest
        void 삭제_요청이_1개_쌓이면_실제로_가게정보가_삭제되지_않는다(DeleteReasonType deleteReasonType) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            storeRepository.save(store);

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), DeleteStoreRequest.testInstance(deleteReasonType), userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.ACTIVE);

            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();
            assertThat(storeDeleteRequestList).hasSize(1);
            assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), userId, deleteReasonType);

            assertThat(response.getIsDeleted()).isFalse();
        }

        @EnumSource
        @ParameterizedTest
        void 삭제_요청이_2개_쌓이면_실제로_가게정보가_삭제되지_않는다(DeleteReasonType deleteReasonType) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.create(store.getId(), 90L, DeleteReasonType.WRONG_CONTENT));

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), DeleteStoreRequest.testInstance(deleteReasonType), userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.ACTIVE);

            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();
            assertThat(storeDeleteRequestList).hasSize(2);
            assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), 90L, DeleteReasonType.WRONG_CONTENT);
            assertStoreDeleteRequest(storeDeleteRequestList.get(1), store.getId(), userId, deleteReasonType);

            assertThat(response.getIsDeleted()).isFalse();
        }

        @EnumSource
        @ParameterizedTest
        void 삭제_요청이_3개_쌓이면_실제로_가게정보가_실제로_삭제된다(DeleteReasonType deleteReasonType) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store.getId(), 1000L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store.getId(), 1001L, DeleteReasonType.NOSTORE))
            );

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), DeleteStoreRequest.testInstance(deleteReasonType), userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertThat(stores).hasSize(1);
            assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.DELETED);

            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();
            assertThat(storeDeleteRequestList).hasSize(3);
            assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), 1000L, DeleteReasonType.NOSTORE);
            assertStoreDeleteRequest(storeDeleteRequestList.get(1), store.getId(), 1001L, DeleteReasonType.NOSTORE);
            assertStoreDeleteRequest(storeDeleteRequestList.get(2), store.getId(), userId, deleteReasonType);

            assertThat(response.getIsDeleted()).isTrue();
        }

        @EnumSource
        @ParameterizedTest
        void 해당_사용자가_해당하는_가게에_대해_이미_삭제요청_한경우_CONFLICT_EXCEPTION(DeleteReasonType reasonType) {
            // given
            Store store = StoreCreator.create(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.create(store.getId(), userId, DeleteReasonType.NOSTORE));

            // when & then
            assertThatThrownBy(() -> storeService.deleteStore(store.getId(), DeleteStoreRequest.testInstance(reasonType), userId))
                .isInstanceOf(ConflictException.class);
        }

    }

    private void assertStoreDeleteRequest(StoreDeleteRequest storeDeleteRequest, Long storeId, Long userId, DeleteReasonType type) {
        assertThat(storeDeleteRequest.getStoreId()).isEqualTo(storeId);
        assertThat(storeDeleteRequest.getUserId()).isEqualTo(userId);
        assertThat(storeDeleteRequest.getReason()).isEqualTo(type);
    }

    private List<DayOfTheWeek> getDayOfTheWeeks(List<AppearanceDay> appearanceDays) {
        return appearanceDays.stream()
            .map(AppearanceDay::getDay)
            .collect(Collectors.toList());
    }

    private List<PaymentMethodType> getPaymentMethodTypes(List<PaymentMethod> paymentMethods) {
        return paymentMethods.stream()
            .map(PaymentMethod::getMethod)
            .collect(Collectors.toList());
    }

    private void assertMenu(Menu menu, String menuName, String price, MenuCategoryType type) {
        assertThat(menu.getName()).isEqualTo(menuName);
        assertThat(menu.getPrice()).isEqualTo(price);
        assertThat(menu.getCategory()).isEqualTo(type);
    }

    private void assertMenu(Menu menu, Long storeId, String menuName, String price, MenuCategoryType type) {
        assertThat(menu.getStore().getId()).isEqualTo(storeId);
        assertThat(menu.getName()).isEqualTo(menuName);
        assertThat(menu.getPrice()).isEqualTo(price);
        assertThat(menu.getCategory()).isEqualTo(type);
    }

    private void assertStore(Store store, Double latitude, Double longitude, String storeName, StoreType storeType, Long userId) {
        assertThat(store.getLocation()).isEqualTo(Location.of(latitude, longitude));
        assertThat(store.getLatitude()).isEqualTo(latitude);
        assertThat(store.getLongitude()).isEqualTo(longitude);
        assertThat(store.getName()).isEqualTo(storeName);
        assertThat(store.getType()).isEqualTo(storeType);
        assertThat(store.getUserId()).isEqualTo(userId);
    }

}
