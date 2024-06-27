package com.maxip.cloudgateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService
{
    @Value("${secret-key}")
    private String SECRET_KEY;

    public String extractUsername(String token) throws JsonProcessingException
    {
        String subject = extractClaim(token, Claims::getSubject);
        return new ObjectMapper().readTree(subject).get("username").asText();
    }

    public Long extractId(String token) throws JsonProcessingException
    {
        String subject = extractClaim(token, Claims::getSubject);
        return new ObjectMapper().readTree(subject).get("id").asLong();
    }

    public void validateToken(final String token)
    {
        Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
    }

    private Key getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }
}

