package com.maxip.codeupload.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.maxip.codeupload.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

@SpringBootTest
public class JWTAuthFilterTest
{
    @MockBean
    private HttpServletRequest httpServletRequestMock;
    @MockBean
    private HttpServletResponse httpServletResponseMock;
    @MockBean
    private FilterChain filterChainMock;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Test
    public void shouldExtractUsernameFromJWT() throws ServletException, IOException
    {
        // given
        when(httpServletRequestMock.getHeader("Authorization")).thenReturn("Bearer jwt...");
        // when
        jwtAuthFilter.doFilterInternal(httpServletRequestMock, httpServletResponseMock, filterChainMock);
        // then
        verify(jwtService).extractUsername("jwt...");
    }

    @Test
    public void shouldThrowUsernameNotFoundException() throws ServletException, IOException
    {
        // given
        String NON_EXISTENT_USER = "nonExistentUserTest";
        when(httpServletRequestMock.getHeader("Authorization")).thenReturn("Bearer jwt...");
        when(jwtService.extractUsername(any(String.class))).thenReturn(NON_EXISTENT_USER);

        // when & then
        assertThrows(UsernameNotFoundException.class, () -> jwtAuthFilter.doFilterInternal(httpServletRequestMock, httpServletResponseMock, filterChainMock));
    }
}
