package com.maxip.filestore.repository;

import com.maxip.filestore.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepo extends JpaRepository<Subject, Long>
{
    Subject findByNameAndUserId(String name, Long userId);

    List<Subject> findAllByUserId(Long userId);
}
