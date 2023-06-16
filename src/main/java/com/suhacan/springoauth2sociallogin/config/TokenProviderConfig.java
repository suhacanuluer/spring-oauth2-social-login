package com.suhacan.springoauth2sociallogin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class TokenProviderConfig {

    private final String jwtSetUri;

    public TokenProviderConfig(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwtSetUri) {
        this.jwtSetUri = jwtSetUri;
    }

    @Bean
    public NimbusJwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwtSetUri).build();
    }

    @Bean
    public TokenProvider tokenProvider(NimbusJwtDecoder jwtDecoder) {
        TokenProvider tokenProvider = new TokenProvider();
        tokenProvider.setJwtDecoder(jwtDecoder);
        return tokenProvider;
    }
}
