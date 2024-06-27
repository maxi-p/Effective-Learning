package com.maxip.noteservice.repository;

import com.maxip.noteservice.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface NoteRepo extends JpaRepository<Note, Long>
{
    List<Note> findAllByUserId(Long userId);
    Set<Note> findAllByUserIdAndNoteCategoryId(Long userId, Long categoryId);
}
