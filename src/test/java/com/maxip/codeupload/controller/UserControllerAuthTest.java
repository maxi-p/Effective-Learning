package com.maxip.codeupload.controller;


import com.maxip.codeupload.service.JwtService;
import com.maxip.codeupload.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(PER_CLASS)
public class UserControllerAuthTest
{
    @Autowired
    private MockMvc mockMvc;
    private String token;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private Environment env;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "moderator", "user"})
    public void shouldAllowAdmin(String endpoint) throws Exception
    {
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(env.getProperty("test.admin")));
        mockMvc.perform(get("/api/v1/"+endpoint).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("nonAdminUsers")
    public void shouldRejectNonAdmins(String username) throws Exception
    {
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(username));
        mockMvc.perform(get("/api/v1/admin").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("nonRegularUsers")
    public void shouldAllowToModerate(String username) throws Exception
    {
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(username));
        mockMvc.perform(get("/api/v1/moderator").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Moderator successful"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "moderator"})
    public void shouldRejectRegularUser(String endpoint) throws Exception
    {
        token = jwtService.generateToken(userDetailsService.loadUserByUsername(env.getProperty("test.username")));
        mockMvc.perform(get("/api/v1/"+endpoint).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    private List<String> nonAdminUsers()
    {
        return Arrays.asList(env.getProperty("test.moderator"), env.getProperty("test.username"));
    }

    private List<String> nonRegularUsers()
    {
        return Arrays.asList(env.getProperty("test.admin"), env.getProperty("test.moderator"));
    }

}
