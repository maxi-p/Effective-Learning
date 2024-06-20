package com.maxip.codeupload.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maxip.codeupload.persistence.entity.CodingProblem;
import com.maxip.codeupload.service.CodingProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/user-service/{username}")
public class CodingProblemController
{

    @Autowired
    private CodingProblemService codingProblemService;

    @GetMapping(value = "/coding-problem")
    public ResponseEntity<Set<CodingProblem>> getCodingProblems(@PathVariable("username") String username)
    {
        Set<CodingProblem> codingProblems = codingProblemService.getCodingProblems(username);
        return ResponseEntity.ok(codingProblems);
    }

    @GetMapping(value = "/coding-problem/{problemId}")
    public ResponseEntity<CodingProblem> getCodingProblemsById(@PathVariable("username") String username, @PathVariable("problemId") Long problemId)  throws JsonProcessingException
    {
        CodingProblem codingProblem = codingProblemService.getCodingProblem(username, problemId);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println(ow.writeValueAsString(codingProblem));
        return ResponseEntity.ok(codingProblem);
    }

    @PostMapping(value = "/coding-problem")
    public ResponseEntity<CodingProblem> createCodingProblem(@PathVariable("username") String username, @RequestBody CodingProblem problem)
    {
        CodingProblem codingProblem = codingProblemService.createCodingProblems(problem, username);
        return ResponseEntity.ok(codingProblem);
    }

    @PutMapping(value = "/coding-problem")

    public ResponseEntity<CodingProblem> updateCodingProblem(@PathVariable("username") String username, @RequestBody CodingProblem problem)
    {
        CodingProblem updatedProblem = codingProblemService.updateCodingProblem(problem, username);
        return ResponseEntity.ok(updatedProblem);
    }

    @DeleteMapping(value = "/coding-problem/{problemId}")
    public ResponseEntity<CodingProblem> deleteCodingProblemById(@PathVariable String username, @PathVariable Long problemId)
    {
        CodingProblem deletedCodingProblem = codingProblemService.deleteCodingProblem(username, problemId);
        return ResponseEntity.ok(deletedCodingProblem);
    }
}
