package com.depromeet.threedollar.api.user.config.interceptor;

import com.depromeet.threedollar.api.user.config.session.SessionConstants;
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class LoginCheckHandler {

    private final SessionRepository<? extends Session> sessionRepository;

    Long getUserId(HttpServletRequest request) {
        String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasLength(sessionId)) {
            Session session = findSessionBySessionId(sessionId);
            Long userId = session.getAttribute(SessionConstants.USER_ID);
            if (userId != null) {
                return userId;
            }
        }
        throw new UnAuthorizedException(String.format("잘못된 세션 (%S) 입니다", sessionId));
    }

    private Session findSessionBySessionId(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new UnAuthorizedException(String.format("잘못된 세션 (%S) 입니다", sessionId));
        }
        return session;
    }

}
