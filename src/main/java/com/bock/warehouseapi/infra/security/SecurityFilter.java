package com.bock.warehouseapi.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bock.warehouseapi.repositories.UserRepository;
import com.bock.warehouseapi.services.impls.TokenServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenServiceImpl tokenService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityFilter(TokenServiceImpl tokenService, UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.recoverToken(request);

            if (token != null) {
                Integer subjectId = tokenService.validateToken(token);

                UserDetails user = userRepository.findBySubjectId(subjectId);

                if (user == null) {
                    throw new JWTVerificationException("Esse token é inválido.");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

                filterChain.doFilter(request, response);
            } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
