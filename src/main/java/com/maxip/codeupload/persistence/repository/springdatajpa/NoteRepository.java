package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long>
{
    public Note findByKeyAndValue(String key, String value);
}
