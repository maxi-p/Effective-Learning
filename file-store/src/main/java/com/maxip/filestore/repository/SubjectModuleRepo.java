package com.maxip.filestore.repository;

import com.maxip.filestore.entity.Subject;
import com.maxip.filestore.entity.SubjectModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectModuleRepo extends JpaRepository<SubjectModule, Long>
{
    SubjectModule findByNameAndSubject(String name, Subject subject);
    List<SubjectModule> findAllBySubject(Subject subject);
}
