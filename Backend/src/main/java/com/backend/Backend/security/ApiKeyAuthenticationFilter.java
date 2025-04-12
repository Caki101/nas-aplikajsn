package com.backend.Backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    public ApiKeyAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // temp logging method for requests to api
        System.err.println(request.getRequestURL() + "  |  " + request.getMethod() + "  |  " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        if (!path.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        String api_key = request.getHeader("Api-Key-Header");

        if (api_key != null && api_key.equals(SecurityData.API_KEY)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "api-user",
                    null,
                    List.of(new SimpleGrantedAuthority(SecurityData.API_USER))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or empty API key");
        }
        filterChain.doFilter(request, response);
    }
}