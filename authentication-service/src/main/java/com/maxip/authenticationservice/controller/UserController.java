package com.maxip.authenticationservice.controller;

import com.maxip.authenticationservice.entity.User;
import com.maxip.authenticationservice.security.AuthenticationResponse;
import com.maxip.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController
{
    @Autowired
    private UserService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user)
    {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User user)
    {
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }
}
