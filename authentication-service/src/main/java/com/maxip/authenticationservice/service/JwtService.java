package com.maxip.authenticationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maxip.authenticationservice.entity.User;
import com.maxip.authenticationservice.entity.UserJwt;
import com.maxip.authenticationservice.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    @Value("${secret-key}")
    private String SECRET_KEY;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepository;

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

    public boolean isTokenValid(String token) throws JsonProcessingException
    {
        final String username = extractUsername(token);
        if (username == null)
        {
            return false;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null)
        {
            return false;
        }
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(String username) throws JsonProcessingException
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) throws JsonProcessingException
    {
        String username = userDetails.getUsername();
        String userId = userRepository.findByUsername(username).getId().toString();
        UserJwt userJwt = new UserJwt(username, userId);

        ObjectWriter ow = new ObjectMapper().writer();
        String userJson = ow.writeValueAsString(userJwt);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userJson).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
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

    private Key getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}

