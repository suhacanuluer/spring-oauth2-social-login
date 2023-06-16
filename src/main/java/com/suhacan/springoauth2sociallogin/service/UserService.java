package com.suhacan.springoauth2sociallogin.service;

import com.suhacan.springoauth2sociallogin.model.CustomOAuth2User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public void processOAuthPostLogin(DefaultOidcUser defaultOidcUser);
}
