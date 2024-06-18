package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

@Entity
public class AlgorithmSubStep
{
    private Long        id;
    private Long        stepNumber;
    private String      description;
    private Algorithm   algorithm;

    public AlgorithmSubStep()
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

    public Long getStepNumber()
    {
        return stepNumber;
    }

    public void setStepNumber(Long stepNumber)
    {
        this.stepNumber = stepNumber;
    }

    @ManyToOne
    public Algorithm getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm)
    {
        this.algorithm = algorithm;
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

