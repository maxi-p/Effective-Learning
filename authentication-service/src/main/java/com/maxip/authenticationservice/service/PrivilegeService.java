package com.maxip.authenticationservice.service;

import com.maxip.authenticationservice.entity.Privilege;
import com.maxip.authenticationservice.repository.PrivilegeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService
{
    @Autowired
    private PrivilegeRepo privilegeRepo;

    public Privilege savePrivilege(Privilege privilege)
    {
        return privilegeRepo.save(privilege);
    }
}
