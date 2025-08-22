package com.cinnamonbay.backend.security.jwt;

import com.cinnamonbay.backend.security.user.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.filter.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HotelUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        logger.debug("Processing request for path: {}", path);

        // Skip JWT validation for public endpoints
        if (isPublicEndpoint(path)) {
            logger.debug("Skipping JWT validation for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getUserNameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Successfully authenticated user: {}", email);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            
        }

        filterChain.doFilter(request, response);
    }

   private boolean isPublicEndpoint(String path) {
    return path.startsWith("/auth") ||
           path.equals("/rooms/all-rooms") ||
           path.equals("/rooms/room/types") ||
           path.startsWith("/rooms/available-rooms") ||
           path.startsWith("/rooms/room/") && path.matches("/rooms/room/\\d+") || // GET room by ID
           path.equals("/error") ||
           path.startsWith("/error/");
}

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}