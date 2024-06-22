package com.maxip.notetestservice.service;

import com.maxip.notetestservice.dto.NoteDto;
import com.maxip.notetestservice.entity.Question;
import com.maxip.notetestservice.entity.Test;
import com.maxip.notetestservice.entity.TestOrder;
import com.maxip.notetestservice.repository.QuestionRepo;
import com.maxip.notetestservice.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Test> getTests()
    {
        return testRepo.findAll();
    }
    public Test getTest(Long id)
    {
        return testRepo.findById(id).orElse(null);
    }

    public Test createTest(TestOrder order)
    {
        System.out.println("Ids: "+order.getCategories());
        int qNum = order.getQuestionNumber();

        NoteDto[] res = restTemplate.postForObject("http://NOTE-SERVICE/api/v1/note/rpc?userId="+order.getUserId(), order.getCategories(), NoteDto[].class);

        List<Long> noteIds = Arrays.stream(res).map(NoteDto::getId).toList();
        HashMap<Long,NoteDto> noteMap = new HashMap<>();
        for(NoteDto dto : res)
        {
            noteMap.put(dto.getId(), dto);
        }

        Random rand  = new Random();
        List<Question> questions = new ArrayList<>();
        List<Long> remaining = new ArrayList<>(noteIds);
        for(int i = 0; i < qNum; i++)
        {
            if (remaining.isEmpty())
            {
                remaining = new ArrayList<>(noteIds);
            }
            int cur = rand.nextInt(remaining.size());
            Long curId = remaining.get(cur);
            remaining.remove(cur);
            NoteDto noteDto = noteMap.get(curId);

            Question question = null;
            if (rand.nextInt(2) == 0)
            {
                question = questionRepo.findByQuestion(noteDto.getKey());
                if (question == null)
                {
                    question = new Question();
                    question.setQuestion(noteDto.getKey());
                    question.setAnswer(noteDto.getValue());
                    questionRepo.save(question);
                }
            }
            else
            {
                question = questionRepo.findByQuestion(noteDto.getValue());
                if (question == null)
                {
                    question = new Question();
                    question.setQuestion(noteDto.getValue());
                    question.setAnswer(noteDto.getKey());
                    questionRepo.save(question);
                }
            }

            questions.add(question);
        }

        Test test = new Test();
        test.setUserId(order.getUserId());
        test.setQuestions(questions);
        test = testRepo.save(test);

        return test;
    }
}
