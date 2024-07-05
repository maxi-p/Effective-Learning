package com.maxip.filestore.repository;

import com.maxip.filestore.entity.File;
import com.maxip.filestore.entity.SubjectModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepo extends JpaRepository<File, Long>
{
    File findByNameAndSubjectModule(String fileName, SubjectModule subjectModule);
    List<File> findAllBySubjectModule(SubjectModule subjectModule);
}
