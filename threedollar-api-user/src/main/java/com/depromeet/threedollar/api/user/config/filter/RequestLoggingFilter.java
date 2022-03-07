package com.depromeet.threedollar.api.user.config.filter;

import com.depromeet.threedollar.api.core.utils.HttpServletRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
class RequestLoggingFilter implements Filter {

    private static final List<String> EXCLUDE_LOGGING_RESPONSE_URLS = List.of("/api/v2/stores/near");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ServletFileUpload.isMultipartContent((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        chain.doFilter(requestWrapper, responseWrapper);
        long end = System.currentTimeMillis();

        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (EXCLUDE_LOGGING_RESPONSE_URLS.contains(requestURI) && responseWrapper.getStatus() == HttpStatus.OK.value()) {
            log.info("\n" +
                    "[REQUEST] {} {} - {}s\n" +
                    "Headers : {}\n",
                HttpServletRequestUtils.getFullUrlWithMethod(requestWrapper), responseWrapper.getStatus(), (end - start) / 1000.0,
                HttpServletRequestUtils.getHeaders((HttpServletRequest) request));
            responseWrapper.copyBodyToResponse();
            return;
        }

        log.info("\n" +
                "[REQUEST] {} - {} - {}s\n" +
                "Headers : {}\n" +
                "Request : {}\n" +
                "Response : {}\n",
            HttpServletRequestUtils.getFullUrlWithMethod(requestWrapper), responseWrapper.getStatus(), (end - start) / 1000.0,
            HttpServletRequestUtils.getHeaders((HttpServletRequest) request),
            HttpServletRequestUtils.getRequestBody(requestWrapper),
            HttpServletRequestUtils.getResponseBody(responseWrapper));
    }

}
