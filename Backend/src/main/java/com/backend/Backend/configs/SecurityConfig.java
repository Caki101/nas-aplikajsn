package com.backend.Backend.configs;

import com.backend.Backend.security.ApiKeyAuthenticationFilter;
import com.backend.Backend.security.JwtAuthenticationFilter;
import com.backend.Backend.security.SecurityData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(ApiKeyAuthenticationFilter apiKeyAuthenticationFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.apiKeyAuthenticationFilter = apiKeyAuthenticationFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> {
                    // disabled for testing purposes
                    // needs to be reorganized
                    //authorizeRequests.requestMatchers("/auth/**").hasAuthority(SecurityData.AUTH_USER);
                    authorizeRequests.requestMatchers("/auth/**").permitAll();
                    authorizeRequests.requestMatchers("/api/**").hasAuthority(SecurityData.API_USER);
                    authorizeRequests.requestMatchers("/public-api/**").permitAll();
                    authorizeRequests.requestMatchers("/pages/**").permitAll();
                    authorizeRequests.requestMatchers("/testing/**").permitAll();
                    authorizeRequests.requestMatchers("/admin/**").permitAll();
                    authorizeRequests.requestMatchers("/admin/**").permitAll();
                    authorizeRequests.requestMatchers("/js/**","/css/**").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiKeyAuthenticationFilter, JwtAuthenticationFilter.class);
        return http.build();
    }
}