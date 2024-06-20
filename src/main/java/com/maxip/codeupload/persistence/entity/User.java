package com.maxip.codeupload.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User
{
    private Long        id;
    private String      username;
    private String      email;
    private String      firstName;
    private String      lastName;
    private String      password;
    private Boolean     locked;
    private Boolean     enabled;
    private Boolean     expired;
    private String      userProfileLatestCode;
    private Set<Note>   notes;
    private Set<Role>   roles;

    // Setters and Getters

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "join_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
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

    @Column(unique = true, nullable = false)
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @OneToMany(mappedBy = "user")
    public Set<Note> getNotes()
    {
        return notes;
    }

    public void setNotes(Set<Note> notes)
    {
        this.notes = notes;
    }

    public String getUserProfileLatestCode()
    {
        return userProfileLatestCode;
    }

    public void setUserProfileLatestCode(String userProfileLatestCode)
    {
        this.userProfileLatestCode = userProfileLatestCode;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getLocked()
    {
        return locked;
    }

    public void setLocked(Boolean locked)
    {
        this.locked = locked;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public Boolean getExpired()
    {
        return expired;
    }

    public void setExpired(Boolean expired)
    {
        this.expired = expired;
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", userProfileLatestCode='" + userProfileLatestCode + '\'' + ", roles=" + roles + '}';
    }
}
