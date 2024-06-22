package com.maxip.authenticationservice.repository;

import com.maxip.authenticationservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long>
{
    public User findByUsername(String username);
}
