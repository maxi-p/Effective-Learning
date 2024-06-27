package com.maxip.noteservice.repository;

import com.maxip.noteservice.entity.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteCategoryRepo extends JpaRepository<NoteCategory, Long>
{
}
