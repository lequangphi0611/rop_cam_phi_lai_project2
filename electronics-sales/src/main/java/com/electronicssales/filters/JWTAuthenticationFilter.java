package com.electronicssales.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.electronicssales.models.UserPrincipal;
import com.electronicssales.services.JwtTokenService;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = jwtTokenService.getJwtTokenFrom(request);
        if(!StringUtils.hasText(jwtToken) || !jwtTokenService.validateToken(jwtToken)) {
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            String username = jwtTokenService.getUsernameFromToken(jwtToken);

            UserPrincipal userPrincipal = (UserPrincipal) userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
    
}