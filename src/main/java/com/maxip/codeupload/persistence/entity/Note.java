package com.maxip.codeupload.persistence.entity;

import jakarta.persistence.*;

@Entity
public class Note
{
    private Long                id;
    private String              key;
    private String              value;
    private User                user;
    private SubjectCategory     subjectCategory;

    public Note()
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

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
    public SubjectCategory getSubjectCategory()
    {
        return subjectCategory;
    }

    public void setSubjectCategory(SubjectCategory subjectCategory)
    {
        this.subjectCategory = subjectCategory;
    }
}
