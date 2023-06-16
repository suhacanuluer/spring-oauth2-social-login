package com.suhacan.springoauth2sociallogin.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class CustomOAuth2User extends DefaultOAuth2User {

    private final OAuth2User oauth2User;

    public CustomOAuth2User(OAuth2User oauth2User) {
        super(oauth2User.getAuthorities(), oauth2User.getAttributes(), "name");
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }
}
