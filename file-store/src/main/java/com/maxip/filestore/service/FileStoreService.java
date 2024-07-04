package com.maxip.filestore.service;

import com.maxip.filestore.config.AmazonConfig;
import com.maxip.filestore.entity.File;
import com.maxip.filestore.entity.Subject;
import com.maxip.filestore.entity.SubjectModule;
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

    public void uploadFile(String userId, String subject, String module, MultipartFile file)
    {
        isFileEmpty(file);
//        isRightMimeType(file);
        Map<String, String> metadata = getMetaData(file);

        String bucket = amazonConfig.BUCKET_NAME;
        saveFile(Long.parseLong(userId), subject, module, file.getOriginalFilename());
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

    public byte[] downloadFile(String filename)
    {
        String filepath = String.format("%s/%s", 1, filename);
        String bucket = amazonConfig.BUCKET_NAME;

        return fileStore.download(bucket, filepath);
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

    private void saveFile(Long userId, String subjectName, String moduleName, String fileName)
    {
        Subject subject = subjectRepo.findByNameAndUserId(subjectName, userId);
        if (subject == null)
        {
            subject = new Subject();
            subject.setUserId(userId);
            subject.setName(subjectName);
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

