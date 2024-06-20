package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.SubjectCategory;
import com.maxip.codeupload.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectCategoryRepository extends JpaRepository<SubjectCategory, Long>
{
    SubjectCategory findByName(String name);
}
