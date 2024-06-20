package com.maxip.codeupload.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Algorithm
{
    private Long                    id;
    private String                  name;
    private String                  description;
    private List<AlgorithmSubStep>  algorithmSubSteps;

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

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy(value="stepNumber")
    public List<AlgorithmSubStep> getAlgorithmSubSteps()
    {
        return algorithmSubSteps;
    }

    public void setAlgorithmSubSteps(List<AlgorithmSubStep> algorithmSubSteps)
    {
        this.algorithmSubSteps = algorithmSubSteps;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
