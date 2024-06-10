package com.maxip.codeupload.security;

import java.util.*;

import com.maxip.codeupload.persistence.entity.Privilege;
import com.maxip.codeupload.persistence.entity.Role;
import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.PrivilegeRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.RoleRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>
{

    public static final String DELETE_PRIVILEGE = "DELETE_PRIVILEGE";
    public static final String WRITE_PRIVILEGE  = "WRITE_PRIVILEGE";
    public static final String READ_PRIVILEGE   = "READ_PRIVILEGE";
    public static final String ROLE_USER        = "ROLE_USER";
    public static final String ROLE_ADMIN       = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR   = "ROLE_MODERATOR";

    private boolean isAlreadyConfigured;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (isAlreadyConfigured)
        {
            return;
        }

        System.out.println("starting configuring");

        Privilege readPrivilege     = createPrivilegeIfNotFound(READ_PRIVILEGE);
        Privilege writePrivilege    = createPrivilegeIfNotFound(WRITE_PRIVILEGE);
        Privilege deletePrivilege   = createPrivilegeIfNotFound(DELETE_PRIVILEGE);

        List<Privilege> adminPrivileges     = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege);
        List<Privilege> managerPrivileges   = Arrays.asList(readPrivilege, writePrivilege);

        createRoleIfNotFound(ROLE_ADMIN, adminPrivileges);
        createRoleIfNotFound(ROLE_MODERATOR, managerPrivileges);
        createRoleIfNotFound(ROLE_USER, Arrays.asList(readPrivilege));

        Role adminRole      = roleRepository.findByName(ROLE_ADMIN);
        Role moderatorRole    = roleRepository.findByName(ROLE_MODERATOR);

        createUserIfNotFound("Max", "Petrushin","maxi-p", adminRole, "asd");
        createUserIfNotFound("Gena", "Matchanov","genjik", adminRole, "asd");

        isAlreadyConfigured = true;
    }

    @Transactional
    private void createUserIfNotFound(String firstname, String lastname, String username, Role role, String password)
    {
        User user = userRepository.findByUsername(username);

        if (user == null)
        {
            user = new User();
            user.setUsername(username);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Arrays.asList(role));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name)
    {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null)
        {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, List<Privilege> privileges)
    {
        Role role = roleRepository.findByName(name);
        if (role == null)
        {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
