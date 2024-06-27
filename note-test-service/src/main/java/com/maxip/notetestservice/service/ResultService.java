package com.maxip.notetestservice.service;

import com.maxip.notetestservice.entity.Result;
import com.maxip.notetestservice.repository.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ResultService
{
    @Autowired
    private ResultRepo resultRepo;

    public List<Result> getResults(Long userId)
    {
        return resultRepo.findAllByUserId(userId);
    }

    public Result getResult(Long resultId)
    {
        return resultRepo.findById(resultId).orElseThrow(() -> new NoSuchElementException("Result not found"));
    }

    public Result createResult(Result result, Long userId)
    {
        String encodedResult = result.getEncodedResult();
        int len = encodedResult.length();
        int count = 0, correctCount = 0;

        for(int i = 0; i < len; i++)
        {
            if(encodedResult.charAt(i) == ',')
            {
                count++;
            }
            if(encodedResult.charAt(i) == 'C')
            {
                correctCount++;
            }
        }

        result.setUserId(userId);
        result.setQuestionNumber(count);
        result.setCorrectQuestionNumber(correctCount);

        return resultRepo.save(result);
    }
}
