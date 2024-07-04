package com.maxip.filestore.repository;

import com.maxip.filestore.entity.Subject;
import com.maxip.filestore.entity.SubjectModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectModuleRepo extends JpaRepository<SubjectModule, Long>
{
    public SubjectModule findByNameAndSubject(String name, Subject subject);
}
