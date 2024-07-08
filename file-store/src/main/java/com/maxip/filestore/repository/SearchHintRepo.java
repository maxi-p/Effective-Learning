package com.maxip.filestore.repository;

import com.maxip.filestore.entity.SearchHint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHintRepo extends JpaRepository<SearchHint, Long>
{
    SearchHint findByValue(String value);
}
