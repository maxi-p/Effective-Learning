package com.maxip.filestore.controller;

import com.maxip.filestore.entity.HintRequest;
import com.maxip.filestore.service.HintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
