package com.maxip.codeupload.persistence.repository.springdatajpa;


import com.maxip.codeupload.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
}
