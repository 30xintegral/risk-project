package com.demo.riskproject.security;

import com.demo.riskproject.dto.response.TokenResponse;
import com.demo.riskproject.entity.RefreshToken;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.repository.RefreshTokenRepository;
import com.demo.riskproject.service.impl.UserServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationInMs}")
    private long jwtExpirationInMs;

    @Value("${jwtRefreshExpirationInMs}")
    private long jwtRefreshExpirationInMs;

    private final UserServiceImpl userService;

    private final RefreshTokenRepository refreshTokenRepository;




    public TokenResponse generateToken(Authentication authentication) {
        String accessToken;
        String refreshToken;
        refreshTokenRepository.deleteAll();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Date refreshDate = new Date(now.getTime() + jwtRefreshExpirationInMs);
        accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuer(userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail())
                .claim("roles", userPrincipal.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        refreshToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuer(userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail())
                .claim("role", userPrincipal.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(refreshDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();


        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
        return new TokenResponse(accessToken, refreshToken);
    }


    public TokenResponse refreshToken(String refreshToken){
        String accessToken;
        String newRefreshToken;
        if (!validateToken(refreshToken)){
            throw new JwtException("Expired or wrong jwt");
        }
        if (refreshTokenRepository.findByRefreshToken(refreshToken).isEmpty()){
            throw new JwtException("Expired or wrong jwt");
        }

        Long id = getUserIdFromJwt(refreshToken);
        UserPrincipal userPrincipal = (UserPrincipal) userService.loadUserById(id);


        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Date refreshExpiryDate = new Date(now.getTime()+jwtRefreshExpirationInMs);
        accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuer(userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail())
                .claim("role", userPrincipal.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        newRefreshToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuer(userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail())
                .claim("roles", userPrincipal.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(refreshExpiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
        refreshTokenRepository.deleteAll();
        RefreshToken RTEntity = new RefreshToken();
        RTEntity.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(RTEntity);
        return new TokenResponse(accessToken,newRefreshToken);
    }




    public Long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }



    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }


}