package com.maxip.notetestservice.repository;

import com.maxip.notetestservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepo extends JpaRepository<Test, Long>
{
    public List<Test> findAllByUserId(Long userId);
}
