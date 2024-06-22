package com.maxip.authenticationservice.repository;

import com.maxip.authenticationservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long>
{
    public Role findByName(String name);
}
