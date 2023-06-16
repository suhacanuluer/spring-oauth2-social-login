package com.suhacan.springoauth2sociallogin.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
//
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException, java.io.IOException {
//        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
//        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Yetkisiz erişim durumunda veya kimlik doğrulama hatası durumunda tetiklenir

        // HTTP yanıtı için durum kodunu belirleme
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;

        // Yanıt başlıklarını yapılandırma
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Yanıt gövdesini oluşturma
        String message = "Yetkisiz erişim. Lütfen giriş yapın.";
        String jsonBody = "{\"status\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Yanıt gövdesini yazma
        PrintWriter writer = response.getWriter();
        writer.write(jsonBody);
        writer.flush();
    }
}
