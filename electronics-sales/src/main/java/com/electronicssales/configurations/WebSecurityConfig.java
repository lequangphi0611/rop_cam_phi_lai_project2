package com.electronicssales.configurations;

import com.electronicssales.filters.JWTAuthenticationFilter;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN_ROLE = Role.ADMIN.toString();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthentication;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
            .authorizeRequests()
            .antMatchers(
                "/api/login", 
                "/api/register"
            )
                .permitAll()
            .antMatchers("/api/images/**")
                .permitAll()
            .antMatchers(
                HttpMethod.GET, 
                "/api/categories/**"
            ).permitAll()
            .antMatchers(HttpMethod.GET, "/api/manufacturers/**")
                .permitAll()
            .antMatchers(HttpMethod.POST, "/api/accounts")
                .hasRole(ADMIN_ROLE)            
            .anyRequest()
            .authenticated()
                .and()
            .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder);
    }    
    
}