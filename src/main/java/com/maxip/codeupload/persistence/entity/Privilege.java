package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Privilege
{
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    public List<Role> roles;

    public Privilege()
    {

    }

    public Privilege(String name)
    {
        this.name = name;
    }

    @ManyToMany(mappedBy = "privileges")
    public List<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(List<Role> roles)
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

    @Column(nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
