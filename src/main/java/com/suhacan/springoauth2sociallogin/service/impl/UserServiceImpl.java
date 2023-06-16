package com.suhacan.springoauth2sociallogin.service.impl;

import com.suhacan.springoauth2sociallogin.model.CustomOAuth2User;
import com.suhacan.springoauth2sociallogin.model.User;
import com.suhacan.springoauth2sociallogin.model.user.GoogleOAuth2UserInfo;
import com.suhacan.springoauth2sociallogin.model.user.OAuth2UserInfo;
import com.suhacan.springoauth2sociallogin.repository.UserRepository;
import com.suhacan.springoauth2sociallogin.service.UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void processOAuthPostLogin(DefaultOidcUser defaultOidcUser) {
        OAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(defaultOidcUser.getAttributes());
        User existUser = userRepository.findByEmail(googleOAuth2UserInfo.getEmail());
        if (existUser == null) {
            createUser(googleOAuth2UserInfo);
        } else {
            updateUser(googleOAuth2UserInfo, existUser);
        }
    }

    private void updateUser(OAuth2UserInfo oAuth2UserInfo, User existUser) {
        existUser.setName(oAuth2UserInfo.getName());
        existUser.setEmail(oAuth2UserInfo.getEmail());
        existUser.setPicture(oAuth2UserInfo.getImageUrl());
        userRepository.save(existUser);
    }

    private void createUser(OAuth2UserInfo oAuth2UserInfo) {
        User newUser = new User();
        updateUser(oAuth2UserInfo, newUser);
    }
}
