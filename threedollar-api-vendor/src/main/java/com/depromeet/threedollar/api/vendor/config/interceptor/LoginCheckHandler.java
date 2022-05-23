package com.depromeet.threedollar.api.vendor.config.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.depromeet.threedollar.api.vendor.config.session.SessionConstants;
import com.depromeet.threedollar.common.exception.model.UnAuthorizedException;

import lombok.RequiredArgsConstructor;

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
        throw new UnAuthorizedException(String.format("인증이 실패하였습니다 - 빈 헤더 (%s) 입니다", sessionId));
    }

    private Session findSessionBySessionId(String sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            throw new UnAuthorizedException(String.format("인증이 실패하였습니다 - 해당 하는 세션(%s)은 존재하지 않습니다", sessionId));
        }
        return session;
    }

}
