package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SubjectCategory
{
    private Long                id;
    private String              name;
    private User                user;
    private List<Note>          notes;
    private List<CodingProblem> codingProblems;

    public SubjectCategory()
    {
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    @OneToMany(mappedBy = "subjectCategory", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    public List<Note> getNotes()
    {
        return notes;
    }

    public void setNotes(List<Note> notes)
    {
        this.notes = notes;
    }

    @ManyToMany(mappedBy = "subjectCategory", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    public List<CodingProblem> getCodingProblems()
    {
        return codingProblems;
    }

    public void setCodingProblems(List<CodingProblem> codingProblems)
    {
        this.codingProblems = codingProblems;
    }
}
