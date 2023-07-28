package com.krab51.webapp.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.util.UUID.randomUUID;

public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull Object handler) {
        String sId = req.getSession(true).getId();
        String rId = randomUUID().toString();

        if (sId.length() > 6)
            MDC.put("CorrelationId", "---" + sId.substring(sId.length() - 6) + ":" + rId.substring(rId.length() - 6));

        return true;
    }
}