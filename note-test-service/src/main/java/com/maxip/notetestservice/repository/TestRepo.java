package com.maxip.notetestservice.repository;

import com.maxip.notetestservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepo extends JpaRepository<Test, Long>
{
}
