package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Entity
public class Role
{
    private String      name;
    private Long        id;
    private List<User>  users;

    public Role()
    {
    }

    @ManyToMany(mappedBy = "roles")
    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    private List<Privilege> privileges;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "join_role_privilege", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    public List<Privilege> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges)
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
