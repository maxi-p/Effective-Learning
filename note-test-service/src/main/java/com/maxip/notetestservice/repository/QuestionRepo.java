package com.maxip.notetestservice.repository;

import com.maxip.notetestservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long>
{
    public Question findByQuestion(String question);
}
