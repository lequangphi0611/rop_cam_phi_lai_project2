package com.electronicssales.services.impls;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.electronicssales.models.UserPrincipal;
import com.electronicssales.services.JwtTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtTokenProvinder implements JwtTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvinder.class);

    private static final String JWT_SECRET = "JWTSuperSecretKey";

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer";

    private long jwtExpirationTime = 0;

    public JwtTokenProvinder(){
    }

    @Override
    public JwtTokenProvinder setJwtExpirationTime(long jwtExpirationTime) {
        this.jwtExpirationTime = jwtExpirationTime;
        return this;
    }

    public Date getNow() {
        return new Date();
    }

    public Date getExpirationTime() {
        return new Date(getNow().getTime() + this.jwtExpirationTime);
    }
 
    @Override
    public String generate(String username) {

        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(getNow())
            .setExpiration(getExpirationTime())
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .compact();
    }

    @Override
    public String generate(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generate(userPrincipal.getUsername());
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts
            .parser()
            .setSigningKey(JWT_SECRET)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts
                .parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(authToken);

            return true;
        } catch (SignatureException ex) {
            LOG.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOG.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOG.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOG.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOG.error("JWT claims string is empty.");
        }
        return false;
    }

    @Override
    public String getJwtTokenFrom(HttpServletRequest req) {
        String token = req.getHeader(HEADER_AUTHORIZATION);
        if(!StringUtils.hasText(token) || !token.startsWith(TOKEN_PREFIX)){
            return null;            
        }

        return token
            .substring(TOKEN_PREFIX.length())
            .trim();
    }

    @Override
    public String getUsernameFromRequest(HttpServletRequest req) {
        return getUsernameFromToken(getJwtTokenFrom(req));
    }

    
}