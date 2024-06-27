package com.maxip.noteservice.service;

import com.maxip.noteservice.entity.NoteCategory;
import com.maxip.noteservice.repository.NoteCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService
{
    @Autowired
    private NoteCategoryRepo noteCategoryRepo;

    public List<NoteCategory> getAllCategories()
    {
        return noteCategoryRepo.findAll();
    }
}
