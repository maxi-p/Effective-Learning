package com.maxip.codeupload.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@PropertySource("classpath:credentials/jwt.properties")
public class JwtServiceTest
{
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private Environment env;

    public String token;

    @BeforeEach
    public void setup()
    {
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(env.getProperty("test.username")));
    }

    @Test
    public void shouldDecodeUsernameTest()
    {
        String username = jwtService.extractUsername(token);
        assertEquals(username, env.getProperty("test.username"));
    }

    @Test
    public void shouldThrowSignatureException()
    {
        char secondToLastChar = token.charAt(token.length() - 2);
        char newSecondToLastChar = (char)((1 + secondToLastChar) % Character.MAX_VALUE);
        String purposefullyWrongToken = token.substring(0, token.length() - 2) + newSecondToLastChar + token.substring(token.length()-1);
        assertThrows(SignatureException.class, () -> jwtService.extractUsername(purposefullyWrongToken));
    }
}
