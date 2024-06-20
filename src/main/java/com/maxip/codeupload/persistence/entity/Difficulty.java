package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

@Entity
public class Difficulty
{
    private Long    id;
    private String  name;

    public Difficulty()
    {
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
