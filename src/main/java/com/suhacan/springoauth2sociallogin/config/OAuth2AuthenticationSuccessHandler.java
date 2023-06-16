package com.suhacan.springoauth2sociallogin.config;

import com.suhacan.springoauth2sociallogin.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    public OAuth2AuthenticationSuccessHandler(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();
        userService.processOAuthPostLogin(defaultOidcUser);
        String token = determineTargetUrl(request, response, authentication);
        JSONObject jo = new JSONObject();
        try {
            jo.put("token", token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " );
            return;
        }
        int status = HttpServletResponse.SC_OK;
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = "Login successful";
        String jsonBody = "{\"status\": " + status + ", \"message\": \"" + message + "\", \"token\": \"" + token + "\"}";

        PrintWriter writer = response.getWriter();
        writer.write(jsonBody);
        writer.flush();
//        response.sendRedirect("/list");
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return tokenProvider.createToken(authentication);
    }
}
