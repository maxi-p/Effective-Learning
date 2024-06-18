package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.CodingProblem;
import com.maxip.codeupload.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingProblemRepository extends JpaRepository<CodingProblem, Long>
{
    public CodingProblem findByNameAndUser(String name, User user);
}
