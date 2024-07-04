package com.maxip.noteservice.repository;

import com.maxip.noteservice.entity.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteCategoryRepo extends JpaRepository<NoteCategory, Long>
{
}
