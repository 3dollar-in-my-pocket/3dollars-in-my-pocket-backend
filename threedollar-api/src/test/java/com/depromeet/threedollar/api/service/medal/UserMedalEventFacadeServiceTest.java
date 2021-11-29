package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.api.service.SetupUserServiceTest;
import com.depromeet.threedollar.domain.domain.medal.UserMedal;
import com.depromeet.threedollar.domain.domain.review.ReviewCreator;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storedelete.DeleteReasonType;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequest;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryCreator;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.threedollar.domain.domain.medal.UserMedalType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class UserMedalEventFacadeServiceTest extends SetupUserServiceTest {

    @Autowired
    private UserMedalEventFacadeService userMedalFacadeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    private StoreDeleteRequestRepository storeDeleteRequestRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VisitHistoryRepository visitHistoryRepository;

    @AfterEach
    void cleanUp() {
        super.cleanup();
        visitHistoryRepository.deleteAll();
        reviewRepository.deleteAll();
        storeDeleteRequestRepository.deleteAll();
        menuRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @Nested
    class 가게_제보시_칭호_획득 {

        @DisplayName("사장님 이분 서비스 주세요 칭호 획득 성공")
        @Test
        void 신규가게를_3번_제보하면_칭호_획득() {
            // given
            for (int i = 0; i < 3; i++) {
                Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
                store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
                storeRepository.save(store);
            }

            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).contains(PLEASE_SERVER_THIS_PERSON),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("사장님 이분 서비스 주세요 칭호 획득 실패")
        @Test
        void 신규가게를_2번_제보하면_사장님_칭호을_얻지_못한다() {
            // given
            for (int i = 0; i < 2; i++) {
                Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
                store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
                storeRepository.save(store);
            }

            userMedalFacadeService.addObtainableMedalsByAddStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @Nested
    class 가게_삭제시_칭호_획득 {

        @DisplayName("우리 동네 보안관 칭호 획득 성공")
        @Test
        void 가게_삭제_제보를_3번_제보하면_칭호_획득() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (long i = 0; i < 3; i++) {
                storeDeleteRequestRepository.save(StoreDeleteRequest.of(i + 1, userId, DeleteReasonType.NOSTORE));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByDeleteStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).contains(MY_NEIGHBORHOOD_SHERIFF),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("우리 동네 보안관 칭호 획득 실패")
        @Test
        void 가게_삭제_제보를_2번_하면_칭호을_얻지_못한다() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (long i = 0; i < 2; i++) {
                storeDeleteRequestRepository.save(StoreDeleteRequest.of(i + 1, userId, DeleteReasonType.NOSTORE));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByDeleteStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @Nested
    class 리뷰_등록시_칭호_획득 {

        @DisplayName("미슐랭 평가단이 바로 나에요 칭호 획득 성공")
        @Test
        void 리뷰를_5번_작성하면_칭호_획득() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 5; i++) {
                reviewRepository.save(ReviewCreator.create(store.getId(), userId, "리뷰", 5));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByAddReview(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).contains(IM_THE_MICHELIN_STA),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("미슐랭 평가단이 바로 나에요 칭호 획득 실패")
        @Test
        void 리뷰_등록을_4번_하면_칭호을_얻지_못한다() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 4; i++) {
                reviewRepository.save(ReviewCreator.create(store.getId(), userId, "리뷰", 5));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByAddReview(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

    }

    @Nested
    class 가게_방문_인증시_칭호_획득 {

        @DisplayName("붕어빵 챌린저 칭호 획득 성공")
        @Test
        void 가게_방문_1번시_붕어빵_챌린저_칭호_획득_성공() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 11, 12)));

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).contains(BUNGEOPPANG_CHALLENGER),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("붕어빵 챌린저 칭호 획득 실패")
        @Test
        void 가게_방문_0번시_붕어빵_챌린저_칭호_획득_실패() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertThat(userMedals).isEmpty();
        }

        @DisplayName("붕어빵 전문가 칭호 획득 성공")
        @Test
        void 가게_방문_3번시_붕어빵_전문가_칭호_획득_성공() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 3; i++) {
                visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 11, i + 1)));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(2),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).containsExactlyInAnyOrder(BUNGEOPPANG_CHALLENGER, BUNGEOPPANG_EXPERT),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("붕어빵 전문가 칭호 획득 실패")
        @Test
        void 가게_방문_2번시_붕어빵_전문가_칭호_획득_실패() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 2; i++) {
                visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 11, i + 1)));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(1),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).doesNotContain(BUNGEOPPANG_EXPERT),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).containsExactlyInAnyOrder(BUNGEOPPANG_CHALLENGER)
            );
        }


        @DisplayName("이 구역 붕친놈은 나야 칭호 획득 성공")
        @Test
        void 가게_방문_10번시_이구역_붕친놈은_나야_획득_성공() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 10; i++) {
                visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 11, i + 1)));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(3),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).contains(BASTARD_IN_THIS_AREA),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).containsExactlyInAnyOrder(BUNGEOPPANG_CHALLENGER, BUNGEOPPANG_EXPERT, BASTARD_IN_THIS_AREA),
                () -> assertThat(userMedals).extracting(UserMedal::getUserId).containsOnly(userId)
            );
        }

        @DisplayName("이 구역 붕친놈은 나야 칭호 획득 실패")
        @Test
        void 가게_방문_9번시_이구역_붕친놈은_나야_획득_실패() {
            // given
            Store store = StoreCreator.create(userId, "가게 이름", 34, 124);
            store.addMenus(List.of(MenuCreator.create(store, "메뉴", "가격", MenuCategoryType.BUNGEOPPANG)));
            storeRepository.save(store);

            for (int i = 0; i < 9; i++) {
                visitHistoryRepository.save(VisitHistoryCreator.create(store, userId, VisitType.EXISTS, LocalDate.of(2021, 11, i + 1)));
            }

            // when
            userMedalFacadeService.addObtainableMedalsByVisitStore(userId);

            // then
            List<UserMedal> userMedals = userMedalRepository.findAll();
            assertAll(
                () -> assertThat(userMedals).hasSize(2),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).doesNotContain(BASTARD_IN_THIS_AREA),
                () -> assertThat(userMedals).extracting(UserMedal::getMedalType).containsExactlyInAnyOrder(BUNGEOPPANG_CHALLENGER, BUNGEOPPANG_EXPERT)
            );
        }

    }

}
