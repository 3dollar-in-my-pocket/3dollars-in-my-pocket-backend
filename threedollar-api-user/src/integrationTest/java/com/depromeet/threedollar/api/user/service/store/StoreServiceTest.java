package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.api.user.service.store.support.StoreAssertions.assertMenu;
import static com.depromeet.threedollar.api.user.service.store.support.StoreAssertions.assertStore;
import static com.depromeet.threedollar.api.user.service.store.support.StoreAssertions.assertStoreDeleteRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.user.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.MenuRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDay;
import com.depromeet.threedollar.domain.rds.user.domain.store.AppearanceDayRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethod;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequest;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreStatus;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreType;

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
        storeDeleteRequestRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
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

    @Nested
    class AddStoreTest {

        @Test
        void 새로운_가게를_등록하면_새로운_가게_데이터가_추가된다() {
            // given
            String storeName = "토끼의 붕어빵";
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
                .menus(Set.of(MenuRequest.of("메뉴 이름", "한 개에 만원", MenuCategoryType.BUNGEOPPANG)))
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
        void 가게_등록시_개장일_데이터도_추가된다() {
            // given
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.SUNDAY);

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
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
        void 가게_등록시_결제방법_데이터도_추가된다() {
            // given
            Set<PaymentMethodType> paymentMethods = Set.of(PaymentMethodType.CASH, PaymentMethodType.ACCOUNT_TRANSFER);

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
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
        void 가게_등록시_메뉴_데이터도_추가된다() {
            // given
            String menuName = "팥 붕어빵";
            String price = "3개에 천원";
            MenuCategoryType type = MenuCategoryType.BUNGEOPPANG;

            Set<MenuRequest> menus = Set.of(MenuRequest.of(menuName, price, type));

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
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
        void 가게_등록시_중복된_메뉴는_한개만_저장된다() {
            // given
            String menuName = "슈크림 붕어빵";
            String price = "2개에 천원";
            MenuCategoryType type = MenuCategoryType.BUNGEOPPANG;

            Set<MenuRequest> menus = new HashSet<>(List.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(menuName, price, type))
            );

            RegisterStoreRequest request = RegisterStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
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
        void 가게의_기본_정보를_수정한다() {
            // given
            String menuName = "슈크림 붕어빵";
            String price = "5개에 2천원";
            MenuCategoryType type = MenuCategoryType.BUNGEOPPANG;

            Store store = StoreCreator.createWithDefaultMenu(userId, "붕어빵 가게");
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
            List<Menu> menus = menuRepository.findAll();
            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertThat(menus).hasSize(1),
                () -> assertMenu(menus.get(0), store.getId(), menuName, price, type)
            );
        }

        @Test
        void 가게의_결제방법을_수정한다() {
            // given
            Set<PaymentMethodType> paymentMethodTypes = Set.of(PaymentMethodType.CARD);

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
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
            assertAll(
                () -> assertThat(paymentMethodsList).hasSize(paymentMethodTypes.size()),
                () -> assertThat(getPaymentMethodTypes(paymentMethodsList)).containsExactlyInAnyOrderElementsOf(paymentMethodTypes)
            );
        }

        @Test
        void 가게의_개시일을_수정한다() {
            // given
            Set<DayOfTheWeek> appearanceDays = Set.of(DayOfTheWeek.SATURDAY, DayOfTheWeek.FRIDAY);

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
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
            assertAll(
                () -> assertThat(appearanceDayList).hasSize(appearanceDays.size()),
                () -> assertThat(getDayOfTheWeeks(appearanceDayList)).containsExactlyInAnyOrderElementsOf(appearanceDays)
            );
        }

        @Test
        void 가게의_메뉴정보를_수정한다() {
            // given
            String menuName = "슈크림 붕어빵";
            String price = "2개에 천원";
            MenuCategoryType type = MenuCategoryType.BUNGEOPPANG;

            Store store = StoreCreator.create(userId, "storeName");
            store.addMenus(List.of(
                MenuCreator.create(store, "메뉴 1", "1000원", MenuCategoryType.DALGONA),
                MenuCreator.create(store, menuName, price, type))
            );
            storeRepository.save(store);

            String newMenuName = "신규 추가된 메뉴";
            String newMenuPrice = "2000원";
            MenuCategoryType newMenuCategory = MenuCategoryType.DALGONA;

            Set<MenuRequest> menuRequests = Set.of(
                MenuRequest.of(menuName, price, type),
                MenuRequest.of(newMenuName, newMenuPrice, newMenuCategory)
            );

            UpdateStoreRequest request = UpdateStoreRequest.testBuilder()
                .latitude(34.0)
                .longitude(130.0)
                .storeName("붕어빵")
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
                () -> assertThat(findMenus).hasSize(2),
                () -> assertMenu(findMenus.get(0), store.getId(), menuName, price, type),
                () -> assertMenu(findMenus.get(1), store.getId(), newMenuName, newMenuPrice, newMenuCategory)
            );
        }

        @Test
        void 가게_메뉴_수정시_중복된_메뉴는_한개만_저장된다() {
            // given
            String menuName = "슈크림 붕어빵";
            String price = "2개에 천원";
            MenuCategoryType type = MenuCategoryType.BUNGEOPPANG;

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
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
            assertAll(
                () -> assertThat(findMenus).hasSize(1),
                () -> assertMenu(findMenus.get(0), store.getId(), menuName, price, type)
            );
        }

        @Test
        void 존재하지_않는_가게를_수정시_NOT_FOUND_STORE_EXCEPTION() {
            // given
            long notFoundStoreId = -1L;

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
        void 내가_등록하지_않은_가게도_수정할수있다_단_제보자는_최초_제보자로_유지된다() {
            // given
            long creatorUserId = 100L;

            Store store = StoreCreator.createWithDefaultMenu(creatorUserId, "storeName");
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
            assertAll(
                () -> assertThat(stores).hasSize(1),
                () -> assertStore(stores.get(0), latitude, longitude, storeName, storeType, creatorUserId)
            );
        }

    }

    @Nested
    class DeleteStoreTest {

        @Test
        void 삭제_요청이_1개_쌓이면_실제로_가게정보가_삭제되지_않는다() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.OVERLAPSTORE;

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
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
        void 삭제_요청이_2개_쌓이면_실제로_가게정보가_삭제되지_않는다() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.OVERLAPSTORE;

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.create(store, 90L, DeleteReasonType.WRONG_CONTENT));

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
        void 삭제_요청이_3개_쌓이면_실제로_가게정보가_실제로_삭제된다() {
            // given
            DeleteReasonType deleteReasonType = DeleteReasonType.WRONG_CONTENT;

            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.saveAll(List.of(
                StoreDeleteRequestCreator.create(store, 1000L, DeleteReasonType.NOSTORE),
                StoreDeleteRequestCreator.create(store, 1001L, DeleteReasonType.NOSTORE))
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
        void 가게_삭제요청시_내가_이미_삭제요청한_가게인경우_CONFLICT_EXCEPTION() {
            // given
            DeleteReasonType reasonType = DeleteReasonType.OVERLAPSTORE;
            Store store = StoreCreator.createWithDefaultMenu(userId, "storeName");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.create(store, userId, DeleteReasonType.NOSTORE));

            DeleteStoreRequest request = DeleteStoreRequest.testBuilder()
                .deleteReasonType(reasonType)
                .build();

            // when & then
            assertThatThrownBy(() -> storeService.deleteStore(store.getId(), request, userId))
                .isInstanceOf(ConflictException.class);
        }

    }

}
