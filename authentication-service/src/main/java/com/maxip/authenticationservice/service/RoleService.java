package com.maxip.authenticationservice.service;

import com.maxip.authenticationservice.entity.Role;
import com.maxip.authenticationservice.repository.PrivilegeRepo;
import com.maxip.authenticationservice.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    @Autowired
    private PrivilegeRepo privilegeRepo;

    @Autowired
    private RoleRepo roleRepo;

    public Role saveRole(Role role)
    {
        role.getPrivileges().forEach(privilege ->
        {
            if (privilege.getId() == null)
            {
                privilegeRepo.save(privilege);
            }
        });
        return roleRepo.save(role);
    }
}
