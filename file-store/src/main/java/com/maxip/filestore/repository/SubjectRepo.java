package com.maxip.filestore.repository;

import com.maxip.filestore.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepo extends JpaRepository<Subject, Long>
{
    public Subject findByNameAndUserId(String name, Long userId);
}
