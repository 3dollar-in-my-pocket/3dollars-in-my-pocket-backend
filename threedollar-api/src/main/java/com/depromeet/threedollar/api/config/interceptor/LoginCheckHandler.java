package com.depromeet.threedollar.api.config.interceptor;

import com.depromeet.threedollar.api.config.session.UserSession;
import com.depromeet.threedollar.common.exception.NotFoundException;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.common.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.depromeet.threedollar.api.config.session.SessionConstants.USER_SESSION;

@RequiredArgsConstructor
@Component
public class LoginCheckHandler {

    private final UserRepository userRepository;

    private final SessionRepository<? extends Session> sessionRepository;

    public Long getUserId(HttpServletRequest request) {
        final String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        final Session session = findSessionBySessionId(sessionId);
        final UserSession userSession = session.getAttribute(USER_SESSION);
        validateExistUser(userSession.getUserId());
        return userSession.getUserId();
    }

    private Session findSessionBySessionId(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new UnAuthorizedException(String.format("잘못된 세션 (%S) 입니다", sessionId));
        }
        return session;
    }

    private void validateExistUser(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("존재하지 않는 유저 (%S) 입니다", userId));
        }
    }

}
