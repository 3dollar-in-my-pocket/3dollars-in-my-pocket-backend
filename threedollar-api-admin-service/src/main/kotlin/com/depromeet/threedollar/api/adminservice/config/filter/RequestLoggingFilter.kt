package com.depromeet.threedollar.api.adminservice.config.filter

import java.util.concurrent.TimeUnit
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import com.depromeet.threedollar.api.core.utils.HttpServletRequestUtils
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class RequestLoggingFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (ServletFileUpload.isMultipartContent(request as HttpServletRequest)) {
            chain.doFilter(request, response)
            return
        }

        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response as HttpServletResponse)
        val start = System.nanoTime()

        chain.doFilter(requestWrapper, responseWrapper)

        val end = System.nanoTime()

        log.info(
            """
            [REQUEST] ${HttpServletRequestUtils.getFullUrlWithMethod(request)} $requestWrapper.status - ${TimeUnit.NANOSECONDS.toMillis(end - start) / 1000.0}s
            Headers : ${HttpServletRequestUtils.getHeaders(request)}
            Request: ${HttpServletRequestUtils.getRequestBody(requestWrapper)}
            Response: ${HttpServletRequestUtils.getResponseBody(responseWrapper)}
            """.trimIndent()
        )
        return
    }

}
