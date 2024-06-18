package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyRepository extends JpaRepository<Difficulty, Long>
{
    Difficulty findByName(String name);
}
