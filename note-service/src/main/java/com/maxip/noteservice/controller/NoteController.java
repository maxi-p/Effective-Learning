package com.maxip.noteservice.controller;

import com.maxip.noteservice.entity.Note;
import com.maxip.noteservice.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/note")
public class NoteController
{
    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> readNotesOfUser(@RequestParam Long userId)
    {
        List<Note> notes = noteService.getAllNotesOfUser(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> readNote(@PathVariable Long noteId)
    {
        Note note = noteService.getNote(noteId);
        return ResponseEntity.ok(note);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note)
    {
        Note createdNote = noteService.createNote(note);
        return ResponseEntity.ok(createdNote);
    }

    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody Note note)
    {
        Note createdNote = noteService.updateNote(note);
        return ResponseEntity.ok(createdNote);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Note> deleteNote(@PathVariable Long noteId)
    {
        Note deletedNote = noteService.deleteNote(noteId);
        return ResponseEntity.ok(deletedNote);
    }

    @PostMapping("/rpc")
    public ResponseEntity<Note[]> readNotesOfCategories(@RequestParam Long userId, @RequestBody List<Long> categoryIds)
    {
        Note[] notes = noteService.getAllNotesOfCategoryOfUser(userId, categoryIds);
        return ResponseEntity.ok(notes);
    }
}
