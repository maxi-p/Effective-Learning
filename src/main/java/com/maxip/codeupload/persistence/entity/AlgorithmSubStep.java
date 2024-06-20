package com.maxip.codeupload.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class AlgorithmSubStep
{
    private Long        id;
    private Long        stepNumber;
    private String      description;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}

