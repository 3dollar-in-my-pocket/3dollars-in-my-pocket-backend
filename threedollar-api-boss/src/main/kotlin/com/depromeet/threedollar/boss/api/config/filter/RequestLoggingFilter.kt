package com.depromeet.threedollar.boss.api.config.filter

import com.depromeet.threedollar.common.utils.logger
import org.springframework.util.StringUtils
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.UnsupportedEncodingException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

class RequestLoggingFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val requestWrapper = ContentCachingRequestWrapper(request as HttpServletRequest)
        val responseWrapper = ContentCachingResponseWrapper(response as HttpServletResponse)
        val start = System.currentTimeMillis()
        chain.doFilter(requestWrapper, responseWrapper)
        val end = System.currentTimeMillis()
        val requestURI = request.requestURI

        log.info("""
            [REQUEST] ${request.method} - $requestURI ${getQueryString(request)} $requestWrapper.status - ${(end - start) / 1000.0}s
            Headers : ${getHeaders(request)}
            Request: ${getRequestBody(requestWrapper)}
            Response: ${getResponseBody(responseWrapper)}
            """.trimIndent())
        return
    }

    private fun getQueryString(request: HttpServletRequest): String {
        val query = request.queryString
        return if (StringUtils.hasText(query)) {
            "?$query"
        } else ""
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()
        val headerArray = request.headerNames
        while (headerArray.hasMoreElements()) {
            val headerName = headerArray.nextElement()
            headerMap[headerName] = request.getHeader(headerName)
        }
        return headerMap
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        val wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper::class.java)
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                return try {
                    String(buf, 0, buf.size)
                } catch (e: UnsupportedEncodingException) {
                    " - "
                }
            }
        }
        return " - "
    }

    private fun getResponseBody(response: HttpServletResponse): String {
        var payload: String? = null
        val wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper::class.java)
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                payload = String(buf, 0, buf.size)
                wrapper.copyBodyToResponse()
            }
        }
        return payload ?: " - "
    }

    companion object {
        val log = logger()
    }

}
