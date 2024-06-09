package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "app_user")
public class User
{
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String userProfileLatestCode;

    private List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
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
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUserProfileLatestCode()
    {
        return userProfileLatestCode;
    }

    public void setUserProfileLatestCode(String userProfileLatestCode)
    {
        this.userProfileLatestCode = userProfileLatestCode;
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", userProfileLatestCode='" + userProfileLatestCode + '\'' + ", roles=" + roles + '}';
    }
}
