package com.maxip.notetestservice.repository;

import com.maxip.notetestservice.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepo extends JpaRepository<Result, Long>
{
    List<Result> findAllByUserId(Long userId);
}
