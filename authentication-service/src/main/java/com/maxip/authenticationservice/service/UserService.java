package com.maxip.authenticationservice.service;

import com.maxip.authenticationservice.entity.User;
import com.maxip.authenticationservice.repository.UserRepo;
import com.maxip.authenticationservice.security.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User user)
    {
        user.getRoles().forEach(role ->
        {
            role.getPrivileges().forEach(privilege ->
            {
                if (privilege.getId() == null)
                {
                    privilegeService.savePrivilege(privilege);
                }
            });
            if (role.getId() == null)
            {
                roleService.saveRole(role);
            }
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepo.save(user);
        String token = jwtService.generateToken(user.getUsername());
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(User user)
    {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );

        if (!auth.isAuthenticated())
        {
            throw new BadCredentialsException("Invalid username or password");
        }

        User search = userRepo.findByUsername(user.getUsername());
        var jwtToken = jwtService.generateToken(search.getUsername());
        return new AuthenticationResponse(jwtToken);
    }

    public boolean validateToken(String token)
    {
        return jwtService.isTokenValid(token);
    }

}
