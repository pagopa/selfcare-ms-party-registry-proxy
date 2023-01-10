package it.pagopa.selfcare.party.registry_proxy.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class LogFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        final String httpUri = httpServletRequest.getRequestURI();
        final String httpMethod = httpServletRequest.getMethod();
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestCacheWrapperObject = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper  responseCacheWrapperObject = new ContentCachingResponseWrapper(httpServletResponse);

        chain.doFilter(requestCacheWrapperObject, responseCacheWrapperObject);

        Long endTime = System.currentTimeMillis() - startTime;
        String requestBody = this.getContentAsString(requestCacheWrapperObject.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = this.getContentAsString(responseCacheWrapperObject.getContentAsByteArray(), response.getCharacterEncoding());
        log.info("Request from URI : {} - method: {} - timelapse: {}ms - Request body {} - Response body {}", httpUri, httpMethod, endTime, requestBody, responseBody);
        responseCacheWrapperObject.copyBodyToResponse();
    }

    private String getContentAsString(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        try {
            return new String(buf, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }

}
