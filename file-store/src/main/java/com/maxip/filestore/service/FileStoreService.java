package com.maxip.filestore.service;

import com.maxip.filestore.config.AmazonConfig;
import com.maxip.filestore.entity.*;
import com.maxip.filestore.repository.FileRepo;
import com.maxip.filestore.repository.SubjectModuleRepo;
import com.maxip.filestore.repository.SubjectRepo;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

@Service
public class FileStoreService
{
    @Autowired
    private AmazonConfig amazonConfig;
    @Autowired
    private FileStore fileStore;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private SubjectModuleRepo subjectModuleRepo;
    @Autowired
    private FileRepo fileRepo;

    public void uploadFile(String userId, String subject, String alias, String module, MultipartFile file)
    {
        isFileEmpty(file);
//        isRightMimeType(file);
        Map<String, String> metadata = getMetaData(file);

        String bucket = amazonConfig.BUCKET_NAME;
        saveFile(Long.parseLong(userId), subject, alias, module, file.getOriginalFilename());
        String filepath = String.format("%s/%s/%s/%s", userId, subject, module, file.getOriginalFilename());

        try
        {
            fileStore.save(bucket, filepath, Optional.of(metadata), file.getInputStream());
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Something is wrong with the file stream: ", e);
        }
    }

    public byte[] download(String loggedId, String subjectId, String moduleId, String filename)
    {
        String bucket = amazonConfig.BUCKET_NAME;
        Long userId = Long.parseLong(loggedId);
        Subject subject = subjectRepo.findById(Long.parseLong(subjectId)).orElseThrow(() -> new IllegalStateException("No such subject found"));
        if (!subject.getUserId().equals(userId))
        {
            throw new IllegalStateException("User is not owner of this subject");
        }
        SubjectModule module = subjectModuleRepo.findById(Long.parseLong(moduleId)).orElseThrow(() -> new IllegalStateException("No such module found"));
        File file = fileRepo.findByNameAndSubjectModule(filename, module);
        if (file == null)
        {
            throw new IllegalStateException("No such file found");
        }
        String filepath = String.format("%s/%s/%s/%s", userId, subject.getName(), module.getName(), filename);
        return fileStore.download(bucket, filepath);
    }
    public byte[] downloadFile(String userId, String fileId)
    {
        File file = fileRepo.findById(Long.parseLong(fileId)).orElseThrow(() -> new IllegalStateException("File not found"));
        SubjectModule subjectModule = file.getSubjectModule();
        Subject subject = subjectModule.getSubject();
        String filepath = String.format("%s/%s/%s/%s", userId, subject.getName(), subjectModule.getName(), file.getName());
        String bucket = amazonConfig.BUCKET_NAME;

        return fileStore.download(bucket, filepath);
    }

    public List<Subject> getAllSubjects(String id)
    {
        return subjectRepo.findAllByUserId(Long.parseLong(id));
    }

    public List<ModuleResponse> getAllModules(String userId, String subjectId)
    {
        Subject subject = subjectRepo.findById(Long.parseLong(subjectId)).orElseThrow(() -> new IllegalStateException("Subject not found"));
        if (!subject.getUserId().equals(Long.parseLong(userId)))
        {
            throw new IllegalStateException("User does not have access to this subject data");
        }
        List<SubjectModule> modules = subjectModuleRepo.findAllBySubject(subject);
        List<ModuleResponse> moduleResponses = new ArrayList<>();

        for (SubjectModule module : modules)
        {
            ModuleResponse moduleResponse = new ModuleResponse();
            List<File> files = fileRepo.findAllBySubjectModule(module);

            moduleResponse.setId(module.getId());
            moduleResponse.setName(module.getName());
            moduleResponse.setFiles(files);
            moduleResponses.add(moduleResponse);
        }

        return moduleResponses;
    }

    private static Map<String, String> getMetaData(MultipartFile file)
    {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", file.getContentType());
        return metadata;
    }

    private static void isRightMimeType(MultipartFile file)
    {
        if (!Arrays.asList(ContentType.APPLICATION_OCTET_STREAM.getMimeType(),
                ContentType.TEXT_PLAIN.getMimeType()).contains(file.getContentType()))
        {
            throw new IllegalArgumentException("File must be a source code file, but is: "+file.getContentType());
        }
    }

    private static void isFileEmpty(MultipartFile file)
    {
        if (file == null && file.isEmpty())
        {
            throw new IllegalArgumentException("File is null or empty");
        }
    }

    private void saveFile(Long userId, String subjectName, String subjectAlias, String moduleName, String fileName)
    {
        Subject subject = subjectRepo.findByNameAndUserId(subjectName, userId);
        if (subject == null)
        {
            subject = new Subject();
            subject.setUserId(userId);
            subject.setName(subjectName);
            subject.setAlias(subjectAlias);
            subject = subjectRepo.save(subject);
        }

        SubjectModule module = subjectModuleRepo.findByNameAndSubject(moduleName, subject);
        if (module == null)
        {
            module = new SubjectModule();
            module.setName(moduleName);
            module.setSubject(subject);
            module = subjectModuleRepo.save(module);
        }

        File file = fileRepo.findByNameAndSubjectModule(fileName, module);
        if (file != null)
        {
            throw new RuntimeException("Duplicate file-name within same module");
        }

        file = new File();
        file.setName(fileName);
        file.setSubjectModule(module);
        fileRepo.save(file);
    }
}

