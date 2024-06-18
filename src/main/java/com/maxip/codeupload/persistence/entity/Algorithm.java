package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Algorithm
{
    private Long                    id;
    private String                  name;
    private String                  description;
    private List<AlgorithmSubStep>  algorithmSubSteps;
    private CodingProblem           codingProblem;

    public Algorithm(Long id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Algorithm()
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

    @OneToMany(mappedBy = "algorithm", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<AlgorithmSubStep> getAlgorithmSubSteps()
    {
        return algorithmSubSteps;
    }

    public void setAlgorithmSubSteps(List<AlgorithmSubStep> algorithmSubSteps)
    {
        this.algorithmSubSteps = algorithmSubSteps;
    }

    @ManyToOne
    public CodingProblem getCodingProblem()
    {
        return codingProblem;
    }

    public void setCodingProblem(CodingProblem codingProblem)
    {
        this.codingProblem = codingProblem;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(unique = true, nullable = false)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
