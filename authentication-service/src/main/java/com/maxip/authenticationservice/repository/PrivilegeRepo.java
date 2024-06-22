package com.maxip.authenticationservice.repository;

import com.maxip.authenticationservice.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepo extends JpaRepository<Privilege, Long>
{
    public Privilege findByName(String name);
}
