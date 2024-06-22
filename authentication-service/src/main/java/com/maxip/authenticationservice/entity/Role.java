package com.maxip.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role
{
    @Id
    @GeneratedValue
    private Long        id;
    @Column(unique = true, nullable = false)
    private String      name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Privilege> privileges;
}

