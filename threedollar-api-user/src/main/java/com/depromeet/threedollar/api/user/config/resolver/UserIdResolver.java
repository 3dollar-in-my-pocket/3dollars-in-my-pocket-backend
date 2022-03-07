package com.depromeet.threedollar.api.user.config.resolver;

import com.depromeet.threedollar.api.user.config.interceptor.Auth;
import com.depromeet.threedollar.api.user.config.session.SessionConstants;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
    }

    @NotNull
    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethodAnnotation(Auth.class) == null) {
            throw new InternalServerException("인증이 필요한 컨트롤러 입니다. @Auth 어노테이션을 붙여주세요.");
        }
        Object object = webRequest.getAttribute(SessionConstants.USER_ID, 0);
        if (object == null) {
            throw new InternalServerException(String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }
        return object;
    }

}
