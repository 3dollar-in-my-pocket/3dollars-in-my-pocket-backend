package com.depromeet.threedollar.api.service.medal;

import com.depromeet.threedollar.domain.domain.medal.UserMedalRepository;
import com.depromeet.threedollar.domain.domain.medal.UserMedalType;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserMedalEventService {

    private final UserMedalService userMedalService;

    private final UserMedalRepository userMedalRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    /**
     * 매번 가게, 리뷰, 가게 삭제, 인증을 할 때마다 해당 카운트들을 모두 조회하는 것은 너무 비효율적이지 않나?
     * <p>
     * 1. 오늘 변경된 유저ID 정보들만 Redis에 저장해두었다가 배치 작업을 통해 해당 유저들에게 메달을 부여하는 방법.
     * <p>
     * 2. 좀 더 커스텀하게 관리 (가게 등록시에는 가게에 관련된 메달들만 체크하는 방법) -> 이러면 코드 규모가 커지는 트레이드 오프가 존재함.
     * -> 일단 비동기적으로 처리하긴 하고 + 해당 요청이 조회 만큼 많이 발생하지는 않으니 코드가 간단한게 효율적이지 않나라는 생각중이지만 차후 개선방법 고려해야봐 함.
     */
    @Transactional
    public void checkAvailableGetMedal(Long userId) {
        List<UserMedalType> medalTypes = getNotHasUserMedalTypes(userId);
        if (hasAllMedal(medalTypes)) {
            return;
        }

        for (UserMedalType medalType : medalTypes) {
            if (medalType.isMatched(
                visitHistoryRepository.findCountsByUserId(userId),
                visitHistoryRepository.findCountsByuserIdAndVisitType(userId, VisitType.NOT_EXISTS),
                reviewRepository.findCountsByUserId(userId),
                storeRepository.findCountsByUserId(userId),
                storeDeleteRequestRepository.findCountsByUserId(userId))
            ) {
                userMedalService.addUserMedal(medalType, userId);
            }
        }
    }

    private List<UserMedalType> getNotHasUserMedalTypes(Long userId) {
        List<UserMedalType> userMedalTypes = userMedalRepository.findAllUserMedalTypeByUserId(userId);
        return Arrays.stream(UserMedalType.values())
            .filter(userMedalType -> !userMedalTypes.contains(userMedalType))
            .collect(Collectors.toList());
    }

    private boolean hasAllMedal(List<UserMedalType> medalTypes) {
        return medalTypes.isEmpty();
    }

}
