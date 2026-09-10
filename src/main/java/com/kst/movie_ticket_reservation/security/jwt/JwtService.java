package com.kst.movie_ticket_reservation.security.jwt;

import com.kst.movie_ticket_reservation.security.key.KeyUtil;
import com.kst.movie_ticket_reservation.util.enums.JwtTokenType;
import com.kst.movie_ticket_reservation.util.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtService
{
    @Value("${access.token.exp}")
    private long accessTokenExp;

    @Value("${refresh.token.exp}")
    private long refreshTokenExp;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService() throws Exception
    {
        this.privateKey = KeyUtil.loadPrivateKey();
        this.publicKey = KeyUtil.loadPublicKey();
    }

    public String generateAccessToken(long id, String email, Set<Role> roles)
    {
        return this.buildToken(id, email, roles, JwtTokenType.ACCESS, accessTokenExp);
    }

    public String generateRefreshToken(long id, String email, Set<Role> roles)
    {
        return this.buildToken(id, email, roles, JwtTokenType.REFRESH, refreshTokenExp);
    }

    public String buildToken(long id, String email, Set<Role> roles, JwtTokenType tokenType, long tokenExp)
    {
        return Jwts.builder()
                .claim("id", id)
                .claim("roles", roles)
                .claim("tokenType", tokenType)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExp))
                .signWith(this.privateKey)
                .compact();
    }

    private Claims extractClaims(final String token)
    {
        try
        {
            return Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (final JwtException e)
        {
            throw new JwtException(e.getLocalizedMessage());
        }
    }

    public boolean isTokenExpired(final String token)
    {
        return extractClaims(token).getExpiration()
                .before(new Date());
    }

    public String extractEmail(final String token)
    {
        return extractClaims(token).getSubject();
    }

    public Long extractId(final String token)
    {
        return extractClaims(token).get("id", Long.class);
    }

    public Set<Role> extractRoles(final String token)
    {
        Object rolesObj = extractClaims(token).get("roles");

        if (rolesObj instanceof List<?> roleList)
        {
            return roleList.stream()
                    .map(roleStr -> Role.valueOf(roleStr.toString()))
                    .collect(Collectors.toSet());
        }

        return Set.of();
    }

    public boolean isTokenValid(final String token, final String expectedEmail)
    {
        final String email = extractEmail(token);
        return email.equals(expectedEmail) && !isTokenExpired(token);
    }
}
