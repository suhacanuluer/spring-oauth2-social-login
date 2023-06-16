package com.suhacan.springoauth2sociallogin.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class TokenProvider {
//    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
//
//    private AppProperties appProperties;
//
//    public TokenProvider(AppProperties appProperties) {
//        this.appProperties = appProperties;
//    }
//
//    public String createToken(Authentication authentication) {
//        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();
//
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
//
//        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret());
//        Key key = Keys.hmacShaKeyFor(keyBytes);
//
//        return Jwts.builder().setSubject(userPrincipal.getIdToken().getSubject()).setIssuedAt(new Date()).setExpiration(expiryDate)
//                .signWith(key, SignatureAlgorithm.HS512).compact();
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        // return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//
//        return Jwts.parserBuilder()
//                .setSigningKey(appProperties.getAuth().getTokenSecret())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Long getUserIdFromToken(String token) {
//        return Long.parseLong(getClaimFromToken(token, Claims::getSubject));
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            getAllClaimsFromToken(authToken);
//            return true;
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
//        }
//        catch(Exception ex){
//            logger.error("Invalid JWT signature");
//        }
//        return false;
//    }

    private NimbusJwtDecoder jwtDecoder;

    public void setJwtDecoder (NimbusJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public boolean validateToken(String token) {
        try {
            // JWT'nin doğruluğunu kontrol etmek için JwtParser kullanılır
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException veya IllegalArgumentException durumunda JWT geçerli değil demektir
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        // JWT içeriğini analiz ederek kimlik doğrulama işlemini gerçekleştirin
        Claims claims = jwtDecoder.decode(token).getClaim("authorities");

        // JWT içerisinden kullanıcı bilgilerini alarak Authentication nesnesini oluşturun
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Oluşturulan kimlik doğrulama nesnesini döndürün
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String createToken(Authentication authentication) {
        DefaultOidcUser userPrincipal = (DefaultOidcUser) authentication.getPrincipal();

        Date now = new Date();
        long tokenExpirationMillis = 3600000L;

        Date expiryDate = new Date(now.getTime() + tokenExpirationMillis);
        String secretKey = "yoursecretkey";

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(userPrincipal.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
