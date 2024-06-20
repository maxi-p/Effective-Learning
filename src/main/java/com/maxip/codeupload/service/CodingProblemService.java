package com.maxip.codeupload.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maxip.codeupload.persistence.entity.*;
import com.maxip.codeupload.persistence.repository.springdatajpa.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CodingProblemService
{
    @Autowired
    private CodingProblemRepository codingProblemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlgorithmRepository algorithmRepository;
    @Autowired
    private AlgorithmSubStepRepository algorithmSubStepRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
    @Autowired
    private SubjectCategoryRepository subjectCategoryRepository;

    public Set<CodingProblem> getCodingProblems(String username)
    {
        User user = userRepository.findByUsername(username);
        return codingProblemRepository.findByUser(user);
    }

    public CodingProblem getCodingProblem(String username, Long id)
    {
        User user = userRepository.findByUsername(username);
        return codingProblemRepository.findByIdAndUser(id,user);
    }

    @Transactional
    public CodingProblem createCodingProblems(CodingProblem problem, String username)
    {
        if (problem != null)
        {
            problem.setUser(userRepository.findByUsername(username));

            difficulty(problem, problem.getDifficulty());
            categoryHelper(problem, problem.getSubjectCategory());
            algorithmHelper(problem, problem.getAlgorithms());

            return codingProblemRepository.save(problem);
        }
        return null;
    }

    @Transactional
    public CodingProblem updateCodingProblem(CodingProblem problem, String username)
    {
        CodingProblem codingProblem = codingProblemRepository.findById(problem.getId()).orElse(null);

        if (codingProblem == null)
        {
            throw new NoSuchElementException("Coding problem not found");
        }

        if (!codingProblem.getUser().getUsername().equals(username))
        {
            throw new NoSuchElementException("Coding problem does not belong to given user");
        }

        codingProblem.setName(problem.getName());
        codingProblem.setProblemStatement(problem.getProblemStatement());
        codingProblem.setProblemSolution(problem.getProblemSolution());
        codingProblem.setSolutionFileUrl(problem.getSolutionFileUrl());
        codingProblem.setOriginalProblemUrl(problem.getOriginalProblemUrl());
        codingProblem.setRuntime(problem.getRuntime());
        codingProblem.setMemory(problem.getMemory());
        codingProblem.setSolved(problem.getSolved());

        difficulty(codingProblem, problem.getDifficulty());
        categoryHelper(codingProblem, problem.getSubjectCategory());
        algorithmHelper(codingProblem, problem.getAlgorithms());

        codingProblem = codingProblemRepository.save(codingProblem);
        return codingProblem;
    }

    @Transactional
    public void difficulty(CodingProblem codingProblem, Difficulty difficulty)
    {
        if (difficulty.getId() == null)
        {
            difficulty = difficultyRepository.save(difficulty);
            codingProblem.setDifficulty(difficulty);
        }
        else
        {
            Difficulty search = difficultyRepository.findById(difficulty.getId()).orElse(null);
            if (search == null) // Throw
            {
                throw new NoSuchElementException("Difficulty not found");
            }
            search.setName(difficulty.getName());
            search = difficultyRepository.save(search);
            codingProblem.setDifficulty(search);
        }
    }

    @Transactional
    public void categoryHelper(CodingProblem codingProblem, List<SubjectCategory> subjectCategories)
    {
        codingProblem.setSubjectCategory(new ArrayList<>());
        for (SubjectCategory subjectCategory : subjectCategories)
        {
            if (subjectCategory.getId() == null) // CREATE
            {
                subjectCategory = subjectCategoryRepository.save(subjectCategory);
                codingProblem.getSubjectCategory().add(subjectCategory);
            }
            else // UPDATE
            {
                SubjectCategory search = subjectCategoryRepository.findById(subjectCategory.getId()).orElse(null);
                if (search == null) // Throw
                {
                    throw new NoSuchElementException("SubjectCategory not found");
                }
                search.setName(subjectCategory.getName());
                search = subjectCategoryRepository.save(search);
                codingProblem.getSubjectCategory().add(search);
            }
        }
    }

    @Transactional
    public void algorithmHelper(CodingProblem codingProblem, List<Algorithm> algorithmList)
    {
        codingProblem.setAlgorithms(new ArrayList<>());
        if (algorithmList != null)
        {
            for (Algorithm algorithm : algorithmList)
            {
                if (algorithm.getId() == null) // CREATE
                {
                    subStepListHelper(algorithm, algorithm.getAlgorithmSubSteps());
                    algorithm = algorithmRepository.save(algorithm);
                    codingProblem.getAlgorithms().add(algorithm);
                }
                else // UPDATE
                {
                    Algorithm search = algorithmRepository.findById(algorithm.getId()).orElse(null);
                    if (search == null) // THROW
                    {
                        throw new NoSuchElementException("Algorithm not found");
                    }

                    search.setName(algorithm.getName());
                    search.setDescription(algorithm.getDescription());
                    subStepListHelper(search, algorithm.getAlgorithmSubSteps());
                    search = algorithmRepository.save(search);
                    codingProblem.getAlgorithms().add(search);
                }
            }
        }
    }

    @Transactional
    public void subStepListHelper(Algorithm algorithm, List<AlgorithmSubStep> subStepList)
    {
        algorithm.setAlgorithmSubSteps(new ArrayList<>());
        if (subStepList != null)
        {
            for (AlgorithmSubStep subStep : subStepList)
            {
                if (subStep.getId() == null) // CREATE
                {
                    subStep = algorithmSubStepRepository.save(subStep);
                    algorithm.getAlgorithmSubSteps().add(subStep);
                }
                else // UPDATE
                {
                    AlgorithmSubStep search = algorithmSubStepRepository.findById(subStep.getId()).orElse(null);
                    if (search == null) // Throw
                    {
                        throw new NoSuchElementException("AlgorithmSubStep not found");
                    }

                    search.setStepNumber(subStep.getStepNumber());
                    search.setDescription(subStep.getDescription());
                    search = algorithmSubStepRepository.save(search);
                    algorithm.getAlgorithmSubSteps().add(search);
                }
            }
        }
    }

    public CodingProblem deleteCodingProblem(String username, Long id)
    {
        User user = userRepository.findByUsername(username);
        CodingProblem codingProblem = codingProblemRepository.findByIdAndUser(id,user);
        if(codingProblem != null)
        {
            codingProblemRepository.delete(codingProblem);
            return codingProblem;
        }
        return null;
    }
}
