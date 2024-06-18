package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Difficulty
{
    private Long                id;
    private List<CodingProblem> codingProblems;
    private String              name;

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

    @OneToMany(mappedBy = "difficulty", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    public List<CodingProblem> getCodingProblems()
    {
        return codingProblems;
    }

    public void setCodingProblems(List<CodingProblem> codingProblems)
    {
        this.codingProblems = codingProblems;
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
