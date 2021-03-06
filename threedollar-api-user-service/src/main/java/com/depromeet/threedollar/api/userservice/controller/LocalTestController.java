package com.depromeet.threedollar.api.userservice.controller;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.auth.dto.response.LoginResponse;
import com.depromeet.threedollar.api.userservice.service.user.UserService;
import com.depromeet.threedollar.api.userservice.service.user.dto.request.CreateUserRequest;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.common.model.UserMetaValue;
import com.depromeet.threedollar.common.utils.UserMetaSessionUtils;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.depromeet.threedollar.api.userservice.config.session.SessionConstants.USER_ID;

@Profile({"local", "local-docker", "dev", "integration-test"})
@RequiredArgsConstructor
@RestController
public class LocalTestController {

    private static final User testUser = User.newInstance("test-uid", UserSocialType.KAKAO, "관리자 계정");

    private final HttpSession httpSession;

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    private final UserService userService;

    @ApiOperation("[개발 서버용] 테스트를 위한 토큰을 받아옵니다")
    @GetMapping("/test-token")
    public ApiResponse<LoginResponse> getTestSession() {
        User user = userRepository.findUserBySocialIdAndSocialType(testUser.getSocialId(), testUser.getSocialType());
        if (user == null) {
            CreateUserRequest request = CreateUserRequest.builder()
                .socialId("test-uid")
                .socialType(UserSocialType.KAKAO)
                .name("관리자 계정")
                .build();

            Long userId = userService.registerUser(request);
            httpSession.setAttribute(USER_ID, userId);
            return ApiResponse.success(LoginResponse.of(httpSession.getId(), userId));
        }
        httpSession.setAttribute(USER_ID, user.getId());
        return ApiResponse.success(LoginResponse.of(httpSession.getId(), user.getId()));
    }

    @ApiOperation("[개발 서버용] 가게를 강제 삭제 합니다")
    @DeleteMapping("/test-store")
    public ApiResponse<String> deleteStoreByForce(
        @RequestParam Long storeId
    ) {
        Store store = StoreServiceHelper.findStoreById(storeRepository, storeId);
        store.deleteByUser();
        storeRepository.save(store);
        return ApiResponse.OK;
    }

    @ApiOperation("[개발 서버용] 서버 에러를 발생시킵니다.")
    @GetMapping("/test-error")
    public ApiResponse<String> throwServerException() {
        throw new InternalServerException("[개발환경 에러 테스트용] 서버 에러가 발생하였습니다");
    }

    @ApiOperation("유저 계정의 디바이스 정보를 가져온다")
    @GetMapping("/test-device")
    public ApiResponse<UserMetaValue> getUserMetaValue() {
        return ApiResponse.success(UserMetaSessionUtils.get());
    }

}
