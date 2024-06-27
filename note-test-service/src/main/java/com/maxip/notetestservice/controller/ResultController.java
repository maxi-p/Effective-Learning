package com.maxip.notetestservice.controller;

import com.maxip.notetestservice.entity.Result;
import com.maxip.notetestservice.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/note-test/result")
public class ResultController
{
    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<Result>> getResults(@RequestHeader("loggedId") String userId)
    {
        List<Result> results = resultService.getResults(Long.parseLong(userId));
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{resultId}")
    public ResponseEntity<Result> readResult(@PathVariable Long resultId)
    {
        Result result = resultService.getResult(resultId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody Result result, @RequestHeader("loggedId") String id)
    {
        Result createdResult = resultService.createResult(result, Long.parseLong(id));
        return ResponseEntity.ok(createdResult);
    }
}
