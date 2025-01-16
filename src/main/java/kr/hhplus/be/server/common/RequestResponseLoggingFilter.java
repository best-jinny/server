package kr.hhplus.be.server.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            try {
                logRequestDetails(httpRequest);
                chain.doFilter(request, response);
            } catch (Exception e) {
                logErrorDetails(httpRequest, e);
                throw e;
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String headers = getHeaders(request);
        String params = getRequestParameters(request);

        log.debug("Request URI: {}", uri);
        log.debug("Request Method: {}", method);
        log.debug("Request Headers: {}", headers);
        log.debug("Request Parameters: {}", params);
    }

    private void logErrorDetails(HttpServletRequest request, Exception e) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.error("Error occurred during request processing");
        log.error("Request URI: {}", uri);
        log.error("Request Method: {}", method);
        log.error("Exception: ", e);
    }

    private String getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }
        return headers.toString();
    }

    private String getRequestParameters(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));
    }
}
