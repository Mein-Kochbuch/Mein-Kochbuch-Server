package com.github.flooooooooooorian.meinkochbuch.security.filter;

import com.github.flooooooooooorian.meinkochbuch.security.models.ChefUser;
import com.github.flooooooooooorian.meinkochbuch.security.service.JwtUtilsService;
import com.github.flooooooooooorian.meinkochbuch.security.service.UserSecurityService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtilsService jwtUtilsService;
    private final UserSecurityService userSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getAuthToken(request);

        if (token != null && !token.isBlank()) {
            try {
                Claims claims = this.jwtUtilsService.parseClaim(token);
                setSecurityContext(claims.getSubject());
            } catch (Exception e) {
                log.error("invalid token", e);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(String subject) {
        ChefUser user = userSecurityService.loadUserById(subject);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private String getAuthToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            return authorization.replace("Bearer ", "").trim();
        }
        return null;
    }
}
