package com.maxip.codeupload.security;

import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PathVarSecurity
{
    @Autowired
    private UserRepository userRepository;

    public boolean checkUsername(Authentication authentication, int userId)
    {
        User user = userRepository.findById(userId);
        return authentication.getName().equals(user.getUsername());
    }
}
