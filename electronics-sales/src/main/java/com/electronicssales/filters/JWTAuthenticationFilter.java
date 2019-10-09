package com.electronicssales.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.electronicssales.models.UserPrincipal;
import com.electronicssales.services.JwtTokenService;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Lazy
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Lazy
    @Autowired
    private JwtTokenService jwtTokenService;

    @Lazy
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = jwtTokenService.getJwtTokenFrom(request);
        if(!StringUtils.hasText(jwtToken) || !jwtTokenService.validateToken(jwtToken)) {
            AuthenticateUtils.clear();
        } else {
            String username = jwtTokenService.getUsernameFromToken(jwtToken);

            UserPrincipal userPrincipal = (UserPrincipal) userService.loadUserByUsername(username);

            LOGGER.info("Authentication with User : ' {} '", userPrincipal);

            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            AuthenticateUtils.setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    
}