package com.maxip.codeupload.persistence.repository.springdatajpa;

import com.maxip.codeupload.persistence.entity.Note;
import com.maxip.codeupload.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NoteRepository extends JpaRepository<Note, Long>
{
    public Note findByKeyAndValue(String key, String value);
    public Note findByIdAndUser(long id, User user);
    public Set<Note> findByUser(User user);
}
