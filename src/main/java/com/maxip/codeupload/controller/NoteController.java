package com.maxip.codeupload.controller;

import com.maxip.codeupload.persistence.entity.CodingProblem;
import com.maxip.codeupload.persistence.entity.Note;
import com.maxip.codeupload.service.CodingProblemService;
import com.maxip.codeupload.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("api/v1/user-service/{username}")
public class NoteController
{
    @Autowired
    private NoteService noteService;

    @GetMapping(value = "/note")
    public ResponseEntity<Set<Note>> getNotes(@PathVariable String username)
    {
        Set<Note> notes = noteService.getNotesOfUser(username);
        return ResponseEntity.ok(notes);
    }

    @GetMapping(value = "/note/{noteId}")
    public ResponseEntity<Note> getNoteById(@PathVariable String username, @PathVariable Long noteId)
    {
        Note notes = noteService.getNote(noteId, username);
        return ResponseEntity.ok(notes);
    }

    @PostMapping(value = "/note")
    public ResponseEntity<Long> addNote(@PathVariable String username, @RequestBody Note note)
    {
        Long newNote = noteService.addNote(note, username);
        return ResponseEntity.ok(newNote);
    }

    @PutMapping(value = "/note")
    public ResponseEntity<Note> updateNote(@PathVariable String username, @RequestBody Note note)
    {
        Note updatedNote = noteService.updateNote(note, username);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping(value = "/note/{noteId}")
    public ResponseEntity<Long> deleteNoteById(@PathVariable String username, @PathVariable Long noteId)
    {
        Long deletedNoteId = noteService.deleteNote(noteId, username);
        return ResponseEntity.ok(deletedNoteId);
    }
}
