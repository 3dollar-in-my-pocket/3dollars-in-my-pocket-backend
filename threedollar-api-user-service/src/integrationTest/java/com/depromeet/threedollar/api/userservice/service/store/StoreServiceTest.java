package com.depromeet.threedollar.api.userservice.service.store;

import com.depromeet.threedollar.api.userservice.SetupUserIntegrationTest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.AppearanceDay;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethod;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequest;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.depromeet.threedollar.api.userservice.service.store.support.StoreAssertions.assertMenu;
import static com.depromeet.threedollar.api.userservice.service.store.support.StoreAssertions.assertStore;
import static com.depromeet.threedollar.api.userservice.service.store.support.StoreAssertions.assertStoreDeleteRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StoreServiceTest extends SetupUserIntegrationTest {

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

    @Nested
    class AddStoreTest {

        @Test
        void ?????????_?????????_???????????????() {
            // given
            String storeName = "????????? ?????????";
            StoreType storeType = StoreType.STORE;
            double latitude = 34.0;
            double longitude = 130.0;

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("?????? ??????", "??? ?????? ??????", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeService.registerStore(request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertStore(stores.get(0), latitude, longitude, storeName, storeType, userId)
            );
        }

        @Test
        void ??????_?????????_?????????_????????????_????????????() {
            // given
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.SUNDAY);

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(appearanceDays)
                .paymentMethods(Collections.emptySet())
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.registerStore(request, userId);

            // then
            List<AppearanceDay> appearanceDayList = appearanceDayRepository.findAll();
            assertAll(
                () -> assertThat(appearanceDayList).hasSize(appearanceDays.size()),
                () -> assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays)
            );
        }

        @Test
        void ??????_?????????_????????????_????????????_????????????() {
            // given
            Set<PaymentMethodType> paymentMethods = Set.of(PaymentMethodType.CASH, PaymentMethodType.ACCOUNT_TRANSFER);

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(paymentMethods)
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.registerStore(request, userId);

            // then
            List<PaymentMethod> paymentMethodsList = paymentMethodRepository.findAll();
            assertAll(
                () -> assertThat(paymentMethodsList).hasSize(paymentMethods.size()),
                () -> assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethods)
            );
        }

        @Test
        void ??????_?????????_??????_????????????_????????????() {
            // given
            String menuName = "??? ?????????";
            String price = "3?????? ??????";
            UserMenuCategoryType type = UserMenuCategoryType.BUNGEOPPANG;

            Set<MenuRequest> menus = Set.of(MenuRequest.of(menuName, price, type));

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.registerStore(request, userId);

            // then
            List<Menu> menuList = menuRepository.findAll();
            assertAll(
                () -> assertThat(menuList).hasSize(1),
                () -> assertMenu(menuList.get(0), menuName, price, type)
            );
        }

        @Test
        void ??????_?????????_?????????_?????????_?????????_????????????() {
            // given
            String menuName = "????????? ?????????";
            String price = "2?????? ??????";
            UserMenuCategoryType type = UserMenuCategoryType.BUNGEOPPANG;

            Set<MenuRequest> menus = new HashSet<>(List.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(menuName, price, type))
            );

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.registerStore(request, userId);

            // then
            List<Menu> menuList = menuRepository.findAll();
            assertAll(
                () -> assertThat(menuList).hasSize(1),
                () -> assertMenu(menuList.get(0), menuName, price, type)
            );
        }

    }

    @Nested
    class UpdateStoreTest {

        @Test
        void ?????????_??????_?????????_???????????????() {
            // given
            String menuName = "????????? ?????????";
            String price = "5?????? 2??????";
            UserMenuCategoryType type = UserMenuCategoryType.BUNGEOPPANG;

            Store store = StoreFixture.createWithDefaultMenu(userId, "????????? ??????");
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;

            Set<MenuRequest> menu = Set.of(MenuRequest.of(menuName, price, type));

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName("?????? ??????")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(menu)
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Store> stores = storeRepository.findAll();
            List<Menu> menus = menuRepository.findAll();
            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertThat(menus).hasSize(1),
                () -> assertMenu(menus.get(0), store.getId(), menuName, price, type)
            );
        }

        @Test
        void ?????????_???????????????_????????????() {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CARD, PaymentMethodType.ACCOUNT_TRANSFER);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            store.addPaymentMethods(Set.of(PaymentMethodType.CARD));
            storeRepository.save(store);

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(paymentMethodTypes)
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<PaymentMethod> paymentMethodsList = paymentMethodRepository.findAll();
            assertAll(
                () -> assertThat(paymentMethodsList).hasSize(paymentMethodTypes.size()),
                () -> assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethodTypes)
            );
        }

        @Test
        void ?????????_?????????_?????????_????????????() {
            // given
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            store.addAppearanceDays(Set.of(DayOfTheWeek.TUESDAY, DayOfTheWeek.WEDNESDAY));
            storeRepository.save(store);

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(appearanceDays)
                .paymentMethods(Collections.emptySet())
                .menus(Collections.emptySet())
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<AppearanceDay> appearanceDayList = appearanceDayRepository.findAll();
            assertAll(
                () -> assertThat(appearanceDayList).hasSize(appearanceDays.size()),
                () -> assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays)
            );
        }

        @Test
        void ?????????_???????????????_????????????() {
            // given
            String menuName = "????????? ?????????";
            String price = "2?????? ??????";
            UserMenuCategoryType type = UserMenuCategoryType.BUNGEOPPANG;

            Store store = StoreFixture.create(userId);
            store.addMenus(List.of(MenuFixture.create(store, menuName, price, type)));
            storeRepository.save(store);

            String newMenuName = "?????? ????????? ??????";
            String newMenuPrice = "2000???";
            UserMenuCategoryType newMenuCategory = UserMenuCategoryType.DALGONA;

            Set<MenuRequest> menuRequests = Set.of(
                MenuRequest.of(newMenuName, newMenuPrice, newMenuCategory)
            );

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menuRequests)
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Menu> findMenus = menuRepository.findAll();
            assertAll(
                () -> assertThat(findMenus).hasSize(1),
                () -> assertMenu(findMenus.get(0), store.getId(), newMenuName, newMenuPrice, newMenuCategory)
            );
        }

        @Test
        void ??????_??????_?????????_?????????_?????????_?????????_????????????() {
            // given
            String menuName = "????????? ?????????";
            String price = "2?????? ??????";
            UserMenuCategoryType type = UserMenuCategoryType.BUNGEOPPANG;

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            Set<MenuRequest> menus = new HashSet<>(List.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(menuName, price, type))
            );

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Collections.emptySet())
                .paymentMethods(Collections.emptySet())
                .menus(menus)
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Menu> findMenus = menuRepository.findAll();
            assertAll(
                () -> assertThat(findMenus).hasSize(1),
                () -> assertMenu(findMenus.get(0), store.getId(), menuName, price, type)
            );
        }

        @Test
        void ????????????_??????_?????????_?????????_NOT_FOUND_STORE_EXCEPTION() {
            // given
            long notFoundStoreId = -1L;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("?????????")
                .storeType(StoreType.STORE)
                .appearanceDays(Set.of(DayOfTheWeek.TUESDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("?????? ??????", "?????? ??????", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when & then
            assertThatThrownBy(() -> storeService.updateStore(notFoundStoreId, request)).isInstanceOf(NotFoundException.class);
        }

        @Test
        void ??????_????????????_??????_?????????_????????????_??????_????????????_??????_????????????_????????????() {
            // given
            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            double latitude = 34.0;
            double longitude = 130.0;
            String storeName = "?????????";
            StoreType storeType = StoreType.STORE;

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(latitude)
                .longitude(longitude)
                .storeName(storeName)
                .storeType(storeType)
                .appearanceDays(Set.of(DayOfTheWeek.FRIDAY))
                .paymentMethods(Set.of(PaymentMethodType.CARD))
                .menus(Set.of(MenuRequest.of("?????? ??????", "??????", UserMenuCategoryType.BUNGEOPPANG)))
                .build();

            // when
            storeService.updateStore(store.getId(), request);

            // then
            List<Store> stores = storeRepository.findAll();
            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertStore(stores.get(0), latitude, longitude, storeName, storeType, userId)
            );
        }

    }

    @Nested
    class DeleteStoreTest {

        @Test
        void ??????_?????????_1???_?????????_?????????_???????????????_????????????_?????????() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.OVERLAPSTORE;

            Store store = StoreFixture.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(deleteReasonType).build();

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();

            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.ACTIVE),
                () -> assertThat(storeDeleteRequestList).hasSize(1),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), userId, deleteReasonType),
                () -> assertThat(response.getIsDeleted()).isFalse()
            );
        }

        @Test
        void ??????_?????????_2???_?????????_?????????_???????????????_????????????_?????????() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.OVERLAPSTORE;

            Store store = StoreFixture.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestFixture.create(store, 90L, DeleteReasonType.WRONG_CONTENT));

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(deleteReasonType)
                .build();

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();

            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.ACTIVE),

                () -> assertThat(storeDeleteRequestList).hasSize(2),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), 90L, DeleteReasonType.WRONG_CONTENT),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(1), store.getId(), userId, deleteReasonType),

                () -> assertThat(response.getIsDeleted()).isFalse()
            );
        }

        @Test
        void ??????_?????????_3???_?????????_?????????_???????????????_?????????_????????????() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.WRONG_CONTENT;

            Store store = StoreFixture.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestFixture.create(store, 1000L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestFixture.create(store, 1001L, DeleteReasonType.NOSTORE))
            );

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(deleteReasonType)
                .build();

            // when
            StoreDeleteResponse response = storeService.deleteStore(store.getId(), request, userId);

            // then
            List<Store> stores = storeRepository.findAll();
            List<StoreDeleteRequest> storeDeleteRequestList = storeDeleteRequestRepository.findAll();

            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertThat(stores.get(0).getStatus()).isEqualTo(StoreStatus.DELETED),

                () -> assertThat(storeDeleteRequestList).hasSize(3),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(0), store.getId(), 1000L, DeleteReasonType.NOSTORE),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(1), store.getId(), 1001L, DeleteReasonType.NOSTORE),
                () -> assertStoreDeleteRequest(storeDeleteRequestList.get(2), store.getId(), userId, deleteReasonType),

                () -> assertThat(response.getIsDeleted()).isTrue()
            );
        }

        @Test
        void ??????_???????????????_??????_??????_???????????????_???????????????_CONFLICT_EXCEPTION() {
            // given
            DeleteReasonType reasonType = DeleteReasonType.OVERLAPSTORE;
            Store store = StoreFixture.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestFixture.create(store, userId, DeleteReasonType.NOSTORE));

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(reasonType)
                .build();

            // when & then
            assertThatThrownBy(() -> storeService.deleteStore(store.getId(), request, userId))
                .isInstanceOf(ConflictException.class);
        }

    }

}
