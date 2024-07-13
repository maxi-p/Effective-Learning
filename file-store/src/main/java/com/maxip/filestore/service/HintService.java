package com.maxip.filestore.service;

import com.maxip.filestore.entity.*;
import com.maxip.filestore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HintService
{
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private SubjectModuleRepo subjectModuleRepo;
    @Autowired
    private HintFileRepo hintFileRepo;
    @Autowired
    private SearchHintRepo searchHintRepo;

    public List<FileResponse> getFiles(String hintName, String loggedId)
    {
        Long userId = Long.parseLong(loggedId);
        SearchHint hint = searchHintRepo.findByValue(hintName.toLowerCase());
        List<HintFile> hintFiles = hintFileRepo.findAllBySearchHint(hint);
        List<FileResponse> files = new ArrayList<>();
        for (HintFile hintFile : hintFiles)
        {
            File file = hintFile.getFile();
            SubjectModule module = file.getSubjectModule();
            Subject subject = module.getSubject();
            if (!subject.getUserId().equals(userId))
                continue;
            FileResponse fileResponse = new FileResponse();
            fileResponse.setId(file.getId());
            fileResponse.setName(file.getName());
            fileResponse.setNumberOfPages(file.getNumberOfPages());
            fileResponse.setResultPage(hintFile.getPageNumber());
            fileResponse.setModuleId(module.getId());
            fileResponse.setSubjectId(subject.getId());
            files.add(fileResponse);
        }
        return files;
    }

    @Transactional
    public void addHints(HintRequest hintRequest, String loggedId)
    {
        Long userId = Long.parseLong(loggedId);
        Long subjectId = Long.parseLong(hintRequest.getSubjectId());
        Long moduleId = Long.parseLong(hintRequest.getModuleId());
        String filename = hintRequest.getFileName();
        Integer pageNumber = hintRequest.getPageNumber();
        List<String> hints = hintRequest.getHints();

        Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new IllegalStateException("No such subject found"));
        if (!subject.getUserId().equals(userId)) throw new IllegalStateException("User is not owner of this subject");

        SubjectModule module = subjectModuleRepo.findById(moduleId).orElseThrow(() -> new IllegalStateException("No such module found"));
        if (!module.getSubject().getId().equals(subjectId))
            throw new IllegalStateException("This module is not part of given subject");

        File file = fileRepo.findByNameAndSubjectModule(filename, module);
        if (file == null) throw new IllegalStateException("No such file found");

        for (String hintValue : hints)
        {
            SearchHint searchHint = searchHintRepo.findByValue(hintValue.toLowerCase());
            if (searchHint == null)
            {
                searchHint = new SearchHint();
                searchHint.setValue(hintValue.toLowerCase());
                searchHint = searchHintRepo.save(searchHint);
            }

            HintFile hintFile = hintFileRepo.findByFileAndSearchHintAndPageNumber(file, searchHint, pageNumber);
            if (hintFile == null)
            {
                hintFile = new HintFile();
                hintFile.setFile(file);
                hintFile.setSearchHint(searchHint);
                hintFile.setPageNumber(pageNumber);
                hintFile = hintFileRepo.save(hintFile);
            }
        }
    }
}
