package com.depromeet.threedollar.api.userservice.service.medal;

import com.depromeet.threedollar.api.userservice.SetupUserIntegrationTest;
import com.depromeet.threedollar.api.userservice.service.medal.support.UserMedalAssertions;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.Medal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.MedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.medal.UserMedalStatus;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.MenuFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryFixture;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.api.userservice.service.medal.support.UserMedalAssertions.assertUserMedal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMedalFacadeServiceTest extends SetupUserIntegrationTest {

    @Autowired
    private AddUserMedalFacadeService userMedalFacadeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private UserMedalRepository userMedalRepository;

    @DisplayName("가게 추가 조건 메달 획득")
    @Nested
    class AddObtainableMedalsByAddStoreTest {

        @DisplayName("[가게 추가 2] - 가게 추가 2 -> 메달 획득 성공")
        @Test
        void 가게_등록_관련_메달_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_STORE, 2);
            medalRepository.save(medal);

            storeRepository.saveAll(List.of(
                StoreFixture.createWithDefaultMenu(userId, "가게 A"),
                StoreFixture.createWithDefaultMenu(userId, "가게 B")
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> UserMedalAssertions.assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[가게 추가 2] - 가게 추가 1 -> 메달 획득 실패")
        @Test
        void 가게_추가_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_STORE, 2);
            medalRepository.save(medal);

            storeRepository.save(StoreFixture.createWithDefaultMenu(userId, "가게 A"));

            // when
            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @DisplayName("가게 삭제 조건 메달 획득")
    @Nested
    class AddObtainableMedalsByDeleteStoreTest {

        @DisplayName("[가게 삭제 1] - 가게 삭제 1 -> 메달 획득 성공")
        @Test
        void 가게_삭제_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.DELETE_STORE, 1);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestFixture.create(store, userId, DeleteReasonType.OVERLAPSTORE));

            // when
            userMedalFacadeService.addObtainableMedalsByDeleteStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[가게 삭제 1] - 가게 삭제 0 -> 메달 획득 실패")
        @Test
        void 가게_삭제_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.DELETE_STORE, 1);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            // when
            userMedalFacadeService.addObtainableMedalsByDeleteStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @DisplayName("리뷰 작성 조건 메달 획득")
    @Nested
    class AddObtainableMedalsByAddReviewTest {

        @DisplayName("[리뷰 2] - 리뷰 2 -> 메달 획득 성공")
        @Test
        void 리뷰_작성_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_REVIEW, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            reviewRepository.saveAll(List.of(
                ReviewFixture.create(store.getId(), userId),
                ReviewFixture.create(store.getId(), userId)
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByAddReview(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[리뷰 2] - 리뷰 1 -> 메달 획득 실패")
        @Test
        void 리뷰_작성_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_REVIEW, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            reviewRepository.save(ReviewFixture.create(store.getId(), userId, "댓글 A", 5));

            // when
            userMedalFacadeService.addObtainableMedalsByAddReview(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @DisplayName("방문 인증 조건 메달 획득")
    @Nested
    class AddObtainableMedalsByVisitStoreTest {

        @DisplayName("[붕어빵 가게 방문 2] - 붕어빵 가게 방문 2 -> 메달 획득 성공")
        @Test
        void 방문_인증_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.create();
            store.addMenus(List.of(MenuFixture.create(store, "팥 붕어빵 2개", "천원", UserMenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryFixture.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[붕어빵 가게 방문 2] - 붕어빵 가게 방문 1 -> 메달 획득 실패")
        @Test
        void 방문_인증_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.create();
            store.addMenus(List.of(MenuFixture.create(store, "팥 붕어빵 2개", "천원", UserMenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryFixture.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

        @DisplayName("[붕어빵 가게 방문 2] - 붕어빵이 아닌 가게 방문 2 -> 메달 획득 실패")
        @Test
        void 붕어빵을_파는_가게가_아닌경우_카운트에_포함되지_않는다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.create();
            store.addMenus(List.of(MenuFixture.create(store, "팥 붕어빵 2개", "천원", UserMenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryFixture.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[방문 실패2] - 성공 2 -> 메달 획득 성공")
        @Test
        void 방문_인증_실패_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryFixture.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[방문 실패2] - 성공 1, 실패 1 -> 메달 획득 실패")
        @Test
        void 방문_인증_실패_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalFixture.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreFixture.createWithDefaultMenu(userId);
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryFixture.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryFixture.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

}
