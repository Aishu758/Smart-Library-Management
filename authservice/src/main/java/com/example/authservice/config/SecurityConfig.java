package com.example.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Authorization config
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/", "/login", "/loginuser","/userbooks", "/registration",
                            "/css/**", "/js/**", "/images/**", "/api/v1/book/add",
                            "/books", "/selectedBooks", "/selectBook/**","/index").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/user/**").authenticated()
                .anyRequest().authenticated()
            )
            // Login config
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler())
                .permitAll()
            )
            // Logout config
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Disable CSRF if you want (be careful, consider your app's needs)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            String username = authentication.getName();
            if ("admin".equalsIgnoreCase(username)) {
                response.sendRedirect("/admin");  // Admin redirect
            } else {
                response.sendRedirect("/user");  // Redirect to userbooks page after successful login
            }
        };
    }

}