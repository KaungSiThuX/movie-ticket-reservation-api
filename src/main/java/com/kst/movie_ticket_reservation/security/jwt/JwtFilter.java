package com.kst.movie_ticket_reservation.security.jwt;

import com.kst.movie_ticket_reservation.security.UrlPaths;
import com.kst.movie_ticket_reservation.security.security_exceptions.CustomExceptionResponse;
import com.kst.movie_ticket_reservation.security.security_exceptions.CustomExceptionResponseData;
import com.kst.movie_ticket_reservation.security.current.CurrentPerson;
import com.kst.movie_ticket_reservation.security.current.CurrentPersonToken;
import com.kst.movie_ticket_reservation.util.enums.Role;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
    private final UrlPaths urlPaths;
    private final CustomExceptionResponse customExceptionResponse;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException
    {
        String path = request.getRequestURI();

        return Arrays.asList(urlPaths.getWHITE_LIST()).contains(path);
    }

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (token.isEmpty())
        {
            filterChain.doFilter(request, response);
            return;
        }

        try
        {
            String email = this.jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                if (this.jwtService.isTokenValid(token, email))
                {
                    Long id = this.jwtService.extractId(token);
                    Set<Role> roles = this.jwtService.extractRoles(token);

                    CurrentPerson currentPerson = new CurrentPerson(id, email, roles);
                    CurrentPersonToken currentPersonToken = new CurrentPersonToken(currentPerson, roles);
                    SecurityContextHolder.getContext().setAuthentication(currentPersonToken);
                }
                else
                {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        catch (JwtException exception)
        {
            this.customExceptionResponse.response(request, response,
                    new CustomExceptionResponseData(HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(), "UNAUTHORIZED_CODE", "invalid token blah"));

            return;
        }

        filterChain.doFilter(request, response);
    }
}
