package com.maxip.codeupload.service;

import com.maxip.codeupload.persistence.entity.Privilege;
import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.RoleRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import com.maxip.codeupload.security.AuthenticationRequest;
import com.maxip.codeupload.security.AuthenticationResponse;
import com.maxip.codeupload.security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class AuthenticationService
{
    private final UserRepository        userRepository;
    private final RoleRepository        roleRepository;
    private final PasswordEncoder       passwordEncoder;
    private final UserDetailsService    userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService            jwtService;

    public AuthenticationResponse register(RegisterRequest request)
    {
        if (userRepository.findByUsername(request.getUsername()) != null)
        {
            throw new RuntimeException("User with such username already exists: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        return new AuthenticationResponse(jwtToken);
    }

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 AuthenticationManager authenticationManager)
    {
        this.userRepository         = userRepository;
        this.passwordEncoder        = passwordEncoder;
        this.roleRepository         = roleRepository;
        this.jwtService             = jwtService;
        this.userDetailsService     = userDetailsService;
        this.authenticationManager  = authenticationManager;
    }
}
