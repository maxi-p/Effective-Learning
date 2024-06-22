package com.maxip.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "app_user")
public class User
{
    @Id
    @GeneratedValue
    private Long        id;
    @Column(unique = true, nullable = false)
    private String      username;
    private String      password;
    private String      email;
    private String      firstName;
    private String      lastName;
    private Boolean     locked;
    private String      userProfileLatestCode;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role>   roles;

}
