package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.domain.user.domain.medal.*;
import com.depromeet.threedollar.domain.user.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.user.domain.store.*;
import com.depromeet.threedollar.domain.user.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequestCreator;
import com.depromeet.threedollar.domain.user.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.user.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.user.domain.visit.VisitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.testhelper.assertion.UserAssertionHelper.assertUserMedal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    class addObtainableMedalsByAddStore {

        @DisplayName("[가게 추가 2] - 가게 추가 2 -> 메달 획득 성공")
        @Test
        void 가게_추가_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_STORE, 2);
            medalRepository.save(medal);

            storeRepository.saveAll(List.of(
                StoreCreator.createWithDefaultMenu(userId, "가게 A"),
                StoreCreator.createWithDefaultMenu(userId, "가게 B")
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertUserMedal(userMedals.get(0), userId, medal.getId(), UserMedalStatus.IN_ACTIVE)
            );
        }

        @DisplayName("[가게 추가 2] - 가게 추가 1 -> 메달 획득 실패")
        @Test
        void 가게_추가_조건을_만족하지_못하면_메달을_획득하지_못한다() {
            // given
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_STORE, 2);
            medalRepository.save(medal);

            storeRepository.save(StoreCreator.createWithDefaultMenu(userId, "가게 A"));

            // when
            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @DisplayName("가게 삭제 조건 메달 획득")
    @Nested
    class addObtainableMedalsByDeleteStore {

        @DisplayName("[가게 삭제 1] - 가게 삭제 1 -> 메달 획득 성공")
        @Test
        void 가게_삭제_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.DELETE_STORE, 1);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
            storeRepository.save(store);

            storeDeleteRequestRepository.save(StoreDeleteRequestCreator.create(store, userId, DeleteReasonType.OVERLAPSTORE));

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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.DELETE_STORE, 1);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
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
    class addObtainableMedalsByAddReview {

        @DisplayName("[리뷰 2] - 리뷰 2 -> 메달 획득 성공")
        @Test
        void 리뷰_작성_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_REVIEW, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
            storeRepository.save(store);

            reviewRepository.saveAll(List.of(
                ReviewCreator.create(store.getId(), userId, "댓글 A", 5),
                ReviewCreator.create(store.getId(), userId, "댓글 B", 4)
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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.ADD_REVIEW, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
            storeRepository.save(store);

            reviewRepository.save(ReviewCreator.create(store.getId(), userId, "댓글 A", 5));

            // when
            userMedalFacadeService.addObtainableMedalsByAddReview(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @DisplayName("방문 인증 조건 메달 획득")
    @Nested
    class addObtainableMedalsByVisitStore {

        @DisplayName("[붕어빵 가게 방문 2] - 붕어빵 가게 방문 2 -> 메달 획득 성공")
        @Test
        void 방문_인증_조건을_만족하면_해당_메달을_획득한다() {
            // given
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.create(userId, "가게");
            store.addMenus(List.of(MenuCreator.create(store, "팥 붕어빵 2개", "천원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryCreator.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.create(userId, "가게");
            store.addMenus(List.of(MenuCreator.create(store, "팥 붕어빵 2개", "천원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)));

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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_BUNGEOPPANG_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.create(userId, "가게");
            store.addMenus(List.of(MenuCreator.create(store, "팥 붕어빵 2개", "천원", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryCreator.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryCreator.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
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
            Medal medal = MedalCreator.create("붕어빵 챌린지", MedalAcquisitionConditionType.VISIT_NOT_EXISTS_STORE, 2);
            medalRepository.save(medal);

            Store store = StoreCreator.createWithDefaultMenu(userId, "가게");
            storeRepository.save(store);

            visitHistoryRepository.saveAll(List.of(
                VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 1, 1)),
                VisitHistoryCreator.create(store, userId, VisitType.NOT_EXISTS, LocalDate.of(2021, 1, 2))
            ));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

}
