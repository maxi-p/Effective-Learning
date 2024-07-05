package com.maxip.filestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"userId","name"})})
public class Subject
{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="userId")
    @JsonIgnore
    private Long userId;
    private String alias;
}
