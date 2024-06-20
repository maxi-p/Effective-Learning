package com.maxip.codeupload.service;

import com.maxip.codeupload.persistence.entity.Note;
import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.NoteRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NoteService
{
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;


    public Set<Note> getNotesOfUser(String username)
    {
        User user = userRepository.findByUsername(username);
        return noteRepository.findByUser(user);
    }

    public Note getNote(Long id, String username)
    {
        User user = userRepository.findByUsername(username);
        return noteRepository.findByIdAndUser(id, user);
    }

    public Long addNote(Note note, String username)
    {
        if (note != null)
        {
            User user = userRepository.findByUsername(username);
            note.setUser(user);
            note = noteRepository.save(note);
            return note.getId();
        }
        return 0L;
    }

    public Note updateNote(Note note, String username)
    {
        Long id = note.getId();
        Note updatedNote = noteRepository.findById(id).orElse(null);
        if (updatedNote != null)
        {
            updatedNote.setKey(note.getKey());
            updatedNote.setValue(note.getValue());
            updatedNote.setSubjectCategory(updatedNote.getSubjectCategory());
            noteRepository.save(updatedNote);
            return updatedNote;
        }
        return null;
    }

    public Long deleteNote(Long id, String username)
    {
        User user = userRepository.findByUsername(username);
        Note note = noteRepository.findByIdAndUser(id, user);
        if(note != null)
        {
            noteRepository.delete(note);
            return note.getId();
        }
        return 0L;
    }
}
