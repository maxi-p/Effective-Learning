package com.maxip.filestore.controller;

import com.maxip.filestore.entity.File;
import com.maxip.filestore.entity.FileResponse;
import com.maxip.filestore.entity.HintRequest;
import com.maxip.filestore.service.HintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/file-store/search-hint")
public class HintController
{
    @Autowired
    private HintService hintService;

    @PostMapping
    public void createHints(@RequestBody HintRequest hintRequest, @RequestHeader("loggedId") String loggedId)
    {
        hintService.addHints(hintRequest, loggedId);
    }

    @GetMapping("/{hintName}")
    public ResponseEntity<List<FileResponse>> getFiles(@PathVariable String hintName, @RequestHeader("loggedId") String loggedId)
    {
        List<FileResponse> files = hintService.getFiles(hintName, loggedId);
        return ResponseEntity.ok(files);
    }
}
