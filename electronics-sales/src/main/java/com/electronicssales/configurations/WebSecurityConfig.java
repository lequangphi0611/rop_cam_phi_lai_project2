package com.electronicssales.configurations;

import java.util.Arrays;

import com.electronicssales.filters.CustomFilter;
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
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MANAGER_ROLE = Role.MANAGER.toString();

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
            .cors()
                .and()
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
            .antMatchers(HttpMethod.GET,"/api/categories/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/api/manufacturers/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/api/discounts/**")
                .permitAll()  
            .antMatchers(HttpMethod.POST, "/api/accounts")
                .hasRole(MANAGER_ROLE)
            .antMatchers(HttpMethod.HEAD, "/api/accounts/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/api/products/**")
                .permitAll()   
            .antMatchers(HttpMethod.GET, "/api/comments/**")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/api/parameter-types/**")
                .permitAll() 
            .antMatchers("/actuator/*")
                .permitAll()
            .anyRequest()
                .authenticated()
                .and()
            .addFilterBefore(new CustomFilter(), ChannelProcessingFilter.class)
            .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder);
    }    

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    
    
}