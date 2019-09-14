package com.electronicssales.filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.electronicssales.models.UserAccount;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * LoginFilter
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authManager) {
		super(defaultFilterProcessesUrl);
        super.setAuthenticationManager(authManager);
    }

	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(request.getParameter("username"));
        userAccount.setPassword(request.getParameter("password"));
        return getAuthenticationManager()
            .authenticate(
                new UsernamePasswordAuthenticationToken(
                    userAccount.getUsername(),
                    userAccount.getPassword(),
                    Collections.emptyList() 
                )
            );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // TODO Auto-generated method stub
        super.successfulAuthentication(request, response, chain, authResult);
    }

    
}