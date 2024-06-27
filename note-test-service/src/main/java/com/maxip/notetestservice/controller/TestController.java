package com.maxip.notetestservice.controller;


import com.maxip.notetestservice.entity.Test;
import com.maxip.notetestservice.entity.TestOrder;
import com.maxip.notetestservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/note-test")
public class TestController
{
    @Autowired
    private TestService testService;

    @GetMapping
    public ResponseEntity<List<Test>> readTests(@RequestHeader("loggedId") String id)
    {
        List<Test> test = testService.getTests(Long.parseLong(id));
        return ResponseEntity.ok(test);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<Test> readTest(@PathVariable Long testId)
    {
        Test test = testService.getTest(testId);
        return ResponseEntity.ok(test);
    }

    @PostMapping("/order-test")
    public ResponseEntity<Test> createTest(@RequestBody TestOrder order, @RequestHeader("loggedId") String id)
    {
        System.out.println(id);
        Test test = testService.createTest(order, Long.parseLong(id));
        return ResponseEntity.ok(test);
    }
}
