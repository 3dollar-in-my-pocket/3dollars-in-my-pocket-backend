package com.depromeet.threedollar.api.userservice.config.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.depromeet.threedollar.api.userservice.config.session.SessionConstants;
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginCheckHandler {

    private final SessionRepository<? extends Session> sessionRepository;
    private final UserRepository userRepository;

    Long getUserId(HttpServletRequest request) {
        String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(sessionId)) {
            throw new UnAuthorizedException(String.format("인증이 실패하였습니다 - 빈 헤더 (%s) 입니다", sessionId));
        }
        Session session = findSessionBySessionId(sessionId);
        Long userId = session.getAttribute(SessionConstants.USER_ID);
        if (!isValidUserId(userId)) {
            throw new UnAuthorizedException(String.format("인증이 실패하였습니다 - 회원 탈퇴되거나 유효하지 않은 유저(%s) 입니다 세션: (%s)", userId, session));
        }
        return userId;
    }

    private Session findSessionBySessionId(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new UnAuthorizedException(String.format("인증이 실패하였습니다 - 해당 하는 세션(%s)은 존재하지 않습니다", sessionId));
        }
        return session;
    }

    private boolean isValidUserId(@Nullable Long userId) {
        return userId != null && userRepository.existsUserById(userId);
    }

}
