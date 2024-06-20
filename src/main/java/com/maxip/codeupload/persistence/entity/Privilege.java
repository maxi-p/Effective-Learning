package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Privilege
{
    private Long        id;
    private String      name;
    public  Set<Role>  roles;

    public Privilege()
    {
    }

    @ManyToMany(mappedBy = "privileges")
    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
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

    @Column(nullable = false, unique = true)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
