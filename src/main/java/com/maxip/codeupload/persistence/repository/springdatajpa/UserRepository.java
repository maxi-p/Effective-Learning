package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
}
