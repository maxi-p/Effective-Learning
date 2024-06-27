package com.maxip.noteservice.service;

import com.maxip.noteservice.entity.Note;
import com.maxip.noteservice.entity.NoteCategory;
import com.maxip.noteservice.repository.NoteCategoryRepo;
import com.maxip.noteservice.repository.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService
{
    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private NoteCategoryRepo categoryRepo;

    public List<Note> getAllNotes()
    {
        return noteRepo.findAll();
    }

    public List<Note> getAllNotesOfUser(Long userId)
    {
        return noteRepo.findAllByUserId(userId);
    }

    public Note getNote(Long noteId)
    {
        return noteRepo.findById(noteId).orElseThrow(() -> new NoSuchElementException("Note not found"));
    }

    public Note createNote(Note note, String id)
    {
        note.setUserId(Long.parseLong(id));
        if (note.getNoteCategory().getId() == null)
        {
            NoteCategory noteCategory = categoryRepo.save(note.getNoteCategory());
            note.setNoteCategory(noteCategory);
        }
        else if (!categoryRepo.existsById(note.getNoteCategory().getId()))
        {
            throw new NoSuchElementException("Category not found");
        }

        return noteRepo.save(note);
    }

    public Note updateNote(Note note)
    {
        Note updatedNote = noteRepo.findById(note.getId()).orElseThrow(() -> new NoSuchElementException("Note not found"));

        updatedNote.setKey(note.getKey());
        updatedNote.setValue(note.getValue());

        NoteCategory noteCategory;
        if (note.getNoteCategory().getId() == null)
        {
            noteCategory = categoryRepo.save(note.getNoteCategory());
        }
        else
        {
            noteCategory = categoryRepo.findById(note.getNoteCategory().getId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
            noteCategory.setName(note.getNoteCategory().getName());
            noteCategory = categoryRepo.save(noteCategory);
        }

        updatedNote.setNoteCategory(noteCategory);
        return noteRepo.save(updatedNote);
    }

    public Note deleteNote(Long noteId)
    {
        Note deletedNote = noteRepo.findById(noteId).orElseThrow(() -> new NoSuchElementException("Note not found"));
        noteRepo.delete(deletedNote);
        return deletedNote;
    }

    public Note[] getAllNotesOfCategoryOfUser(Long userId, List<Long> categoryIds)
    {
        Set<Note> result = new HashSet<>();
        for(Long categoryId : categoryIds)
        {
            result.addAll(noteRepo.findAllByUserIdAndNoteCategoryId(userId, categoryId));
        }
        return result.toArray(new Note[0]);
    }
}
