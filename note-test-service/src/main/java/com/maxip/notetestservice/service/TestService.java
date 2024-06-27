package com.maxip.notetestservice.service;

import com.maxip.notetestservice.dto.NoteDto;
import com.maxip.notetestservice.entity.Question;
import com.maxip.notetestservice.entity.Test;
import com.maxip.notetestservice.entity.TestOrder;
import com.maxip.notetestservice.repository.QuestionRepo;
import com.maxip.notetestservice.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService
{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private TestRepo testRepo;

    public List<Test> getTests(Long id)
    {
        return testRepo.findAllByUserId(id);
    }
    public Test getTest(Long id)
    {
        return testRepo.findById(id).orElse(null);
    }

    public Test createTest(TestOrder order, Long id)
    {
        NoteDto[] res = restTemplate.postForObject("http://NOTE-SERVICE/api/v1/note/rpc?id="+id.toString(), order.getCategories(), NoteDto[].class);

        List<NoteDto> notes = new ArrayList<>(Arrays.stream(res).toList());

        Random rand  = new Random();
        List<Question> questions = new ArrayList<>();

        int len = notes.size();
        for(int i = 0; i < len; i++)
        {
            int randomIndex = rand.nextInt(notes.size());
            NoteDto noteDto = notes.get(randomIndex);
            notes.remove(randomIndex);

            Question question = questionRepo.findByQuestion(noteDto.getKey());
            if (question == null)
            {
                question = new Question();
                question.setQuestion(noteDto.getKey());
                question.setAnswer(noteDto.getValue());
                questionRepo.save(question);
            }

            questions.add(question);
        }

        Test test = new Test();
        test.setUserId(id);
        test.setNumberOfQuestions(Math.min(order.getQuestionNumber(),len));
        test.setQuestions(questions);
        test = testRepo.save(test);

        return test;
    }
}
