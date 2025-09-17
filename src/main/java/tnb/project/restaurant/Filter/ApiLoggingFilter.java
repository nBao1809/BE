package tnb.project.restaurant.Filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String method = httpRequest.getMethod();
            String uri = httpRequest.getRequestURI();
            String query = httpRequest.getQueryString();
            logger.info("[API] {} {}{}", method, uri, (query != null ? ("?" + query) : ""));
        }
        chain.doFilter(request, response);
    }
}

