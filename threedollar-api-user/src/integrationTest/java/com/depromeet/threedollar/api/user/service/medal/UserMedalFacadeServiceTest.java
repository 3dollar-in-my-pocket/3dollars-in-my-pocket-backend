package com.depromeet.threedollar.api.user.service.medal;

import static com.depromeet.threedollar.api.user.service.medal.support.UserMedalAssertions.assertUserMedal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.depromeet.threedollar.api.user.service.SetupUserServiceTest;
import com.depromeet.threedollar.api.user.service.medal.support.UserMedalAssertions;
import com.depromeet.threedollar.domain.rds.user.domain.medal.Medal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalAcquisitionConditionType;
import com.depromeet.threedollar.domain.rds.user.domain.medal.MedalCreator;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.rds.user.domain.medal.UserMedalStatus;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.DeleteReasonType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreWithMenuCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;

@SpringBootTest
class UserMedalFacadeServiceTest extends SetupUserServiceTest {

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

    @AfterEach
    void cleanUp() {
        super.cleanup();
        visitHistoryRepository.deleteAllInBatch();
        storeDeleteRequestRepository.deleteAllInBatch();
        reviewRepository.deleteAllInBatch();
        storeRepository.deleteAll();
    }

    @DisplayName("가게 추가 조건 메달 획득")
    @Nested
    class AddObtainableMedalsByAddStoreTest {

        @DisplayName("[가게 추가 2] - 가게 추가 2 -> 메달 획득 성공")
        @Test
        void 가게_추가_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.ADD_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            storeRepository.saveAll(List.of(
                StoreWithMenuCreator.builder()
                    .userId(userId)
                    .storeName("등록한 가게 A")
                    .build(),
                StoreWithMenuCreator.builder()
                    .userId(userId)
                    .storeName("등록한 가게 B")
                    .build()
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.ADD_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            storeRepository.save(store);

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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.DELETE_STORE)
                .conditionCount(1)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게 1")
                .build();
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.builder()
                .store(store)
                .userId(userId)
                .reasonType(DeleteReasonType.OVERLAPSTORE)
                .build());

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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.DELETE_STORE)
                .conditionCount(1)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.ADD_REVIEW)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            storeRepository.save(store);

            reviewRepository.saveAll(List.of(
                ReviewCreator.builder()
                    .storeId(store.getId())
                    .userId(userId)
                    .contents("리뷰 작성 1")
                    .rating(5)
                    .build(),
                ReviewCreator.builder()
                    .storeId(store.getId())
                    .userId(userId)
                    .contents("리뷰 작성 2")
                    .rating(4)
                    .build()
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.ADD_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            storeRepository.save(store);

            reviewRepository.save(ReviewCreator.builder()
                .storeId(store.getId())
                .userId(userId)
                .contents("리뷰 작성 1")
                .rating(5)
                .build());

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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            store.addMenus(List.of(
                MenuCreator.builder()
                    .store(store)
                    .name("팥 붕어빵")
                    .price("2개에 천원")
                    .category(MenuCategoryType.BUNGEOPPANG)
                    .build()
            ));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 2))
                    .build()
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            store.addMenus(List.of(
                MenuCreator.builder()
                    .store(store)
                    .name("팥 붕어빵")
                    .price("2개에 천원")
                    .category(MenuCategoryType.BUNGEOPPANG)
                    .build()
            ));
            storeRepository.save(store);

            VisitHistory visitHistory = VisitHistoryCreator.builder()
                .store(store)
                .userId(user.getId())
                .type(VisitType.EXISTS)
                .dateOfVisit(LocalDate.of(2021, 1, 1))
                .build();
            visitHistoryRepository.save(visitHistory);

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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            store.addMenus(List.of(
                MenuCreator.builder()
                    .store(store)
                    .name("팥 붕어빵")
                    .price("2개에 천원")
                    .category(MenuCategoryType.BUNGEOPPANG)
                    .build()
            ));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 2))
                    .build()
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 2))
                    .build()
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
            Medal medal = MedalCreator.builder()
                .name("붕어빵 챌린지")
                .conditionType(MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE)
                .conditionCount(2)
                .build();
            medalRepository.save(medal);

            Store store = StoreWithMenuCreator.builder()
                .userId(userId)
                .storeName("가게")
                .build();
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 1))
                    .build(),
                VisitHistoryCreator.builder()
                    .store(store)
                    .userId(user.getId())
                    .type(VisitType.NOT_EXISTS)
                    .dateOfVisit(LocalDate.of(2021, 1, 2))
                    .build()
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

}
