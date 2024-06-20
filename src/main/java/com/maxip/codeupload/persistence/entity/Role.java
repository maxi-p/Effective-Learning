package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Set;

@Entity
public class Role
{
    private String      name;
    private Long        id;
    private Set<User>  users;

    public Role()
    {
    }

    @ManyToMany(mappedBy = "roles")
    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(Set<User> users)
    {
        this.users = users;
    }

    private Set<Privilege> privileges;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "join_role_privilege", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    public Set<Privilege> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges)
    {
        this.privileges = privileges;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(unique = true, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", privileges=" + privileges + '}';
    }
}
