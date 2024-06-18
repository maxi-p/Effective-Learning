package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CodingProblem
{
    private Long                    id;
    private String                  name;
    private String                  problemStatement;
    private String                  problemSolution;
    private String                  solutionFileUrl;
    private String                  originalProblemUrl;
    private Double                  runtime;
    private Double                  memory;
    private boolean                 solved;
    private User                    user;
    private Difficulty              difficulty;
    private List<SubjectCategory>   subjectCategory;
    private List<Algorithm>         algorithms;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "join_problem_categories",
            joinColumns = @JoinColumn(name = "coding_problem_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_category_id"))
    public List<SubjectCategory> getSubjectCategory()
    {
        return subjectCategory;
    }

    public void setSubjectCategory(List<SubjectCategory> subjectCategory)
    {
        this.subjectCategory = subjectCategory;
    }

    @OneToMany(mappedBy = "codingProblem", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    public List<Algorithm> getAlgorithms()
    {
        return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms)
    {
        this.algorithms = algorithms;
    }

    @ManyToOne
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @ManyToOne
    public Difficulty getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    @Column(unique = true, nullable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProblemStatement()
    {
        return problemStatement;
    }

    public void setProblemStatement(String problemStatement)
    {
        this.problemStatement = problemStatement;
    }

    public String getProblemSolution()
    {
        return problemSolution;
    }

    public void setProblemSolution(String problemSolution)
    {
        this.problemSolution = problemSolution;
    }

    public String getSolutionFileUrl()
    {
        return solutionFileUrl;
    }

    public void setSolutionFileUrl(String solutionFileUrl)
    {
        this.solutionFileUrl = solutionFileUrl;
    }

    public String getOriginalProblemUrl()
    {
        return originalProblemUrl;
    }

    public void setOriginalProblemUrl(String originalProblemUrl)
    {
        this.originalProblemUrl = originalProblemUrl;
    }

    public Double getRuntime()
    {
        return runtime;
    }

    public void setRuntime(Double runtime)
    {
        this.runtime = runtime;
    }

    public Double getMemory()
    {
        return memory;
    }

    public void setMemory(Double memory)
    {
        this.memory = memory;
    }

    public boolean isSolved()
    {
        return solved;
    }

    public void setSolved(boolean solved)
    {
        this.solved = solved;
    }
}
