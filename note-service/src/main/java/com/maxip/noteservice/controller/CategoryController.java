package com.maxip.noteservice.controller;

import com.maxip.noteservice.entity.Note;
import com.maxip.noteservice.entity.NoteCategory;
import com.maxip.noteservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/note-category")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<NoteCategory>> readNotesOfUser()
    {
        List<NoteCategory> noteCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(noteCategories);
    }
}
