package cl.admin.usercrud.configuration;


import cl.admin.usercrud.exception.NotFoundException;
import cl.admin.usercrud.models.UserEntity;
import cl.admin.usercrud.repository.UserRepository;
import cl.admin.usercrud.services.IJWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final IJWTService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().startsWith(request.getContextPath() + "/api/v1/user") &&
             !request.getRequestURI().startsWith(request.getContextPath() + "/api/v1/phone")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String uuid;

        jwt = authHeader.substring(7);
        uuid = jwtService.extracUUID(jwt);



        if(StringUtils.hasLength(uuid) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserEntity user = userRepository.findById(UUID.fromString(uuid)).orElseThrow(() -> new NotFoundException("User not found"));

            if(jwtService.isTokenValid(jwt, user)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }

}
