package com.maxip.filestore.service;

import com.maxip.filestore.config.AmazonConfig;
import com.maxip.filestore.entity.*;
import com.maxip.filestore.repository.FileRepo;
import com.maxip.filestore.repository.SubjectModuleRepo;
import com.maxip.filestore.repository.SubjectRepo;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Transactional
    public void uploadFile(String userId, String subject, String alias, String module, MultipartFile file)
    {
        if (isFileEmpty(file))
            throw new IllegalStateException("File is empty or filename is empty");

        Map<String, String> metadata = getMetaData(file);

        String bucket = amazonConfig.BUCKET_NAME;
        String nameWithoutExtension = getNameWithoutExtension(file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        String filepath = String.format("%s/%s/%s/%s/%s", userId, subject, module, nameWithoutExtension, filename);

        try
        {
            saveFileInDatabase(userId, subject, alias, module, filename);
            fileStore.save(bucket, filepath, Optional.of(metadata), file.getInputStream());
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Something is wrong with the file stream: ", e);
        }
    }

    @Transactional
    public File deleteFile(String loggedId, String sbjId, String mdlId, String filename)
    {
        String bucket = amazonConfig.BUCKET_NAME;
        Long userId = Long.parseLong(loggedId);
        Long subjectId = Long.parseLong(sbjId);
        Long moduleId = Long.parseLong(mdlId);
        String nameWithoutExtension = getNameWithoutExtension(filename);

        Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new IllegalStateException("No such subject found"));
        if (!subject.getUserId().equals(userId)) throw new IllegalStateException("User is not owner of this subject");

        SubjectModule module = subjectModuleRepo.findById(moduleId).orElseThrow(() -> new IllegalStateException("No such module found"));
        if (!module.getSubject().getId().equals(subjectId))
            throw new IllegalStateException("This module is not part of given subject");

        File file = fileRepo.findByNameAndSubjectModule(filename, module);
        if (file == null) throw new IllegalStateException("No such file found");

        fileRepo.delete(file);
        String filePrefix = String.format("%s/%s/%s/%s", userId, subject.getName(), module.getName(), nameWithoutExtension);
        System.out.println(filePrefix);
        fileStore.clearFolder(bucket, filePrefix);

        return file;
    }

    public byte[] download(String loggedId, String subjectId, String moduleId, String filename)
    {
        String bucket = amazonConfig.BUCKET_NAME;
        Long userId = Long.parseLong(loggedId);

        Subject subject = subjectRepo.findById(Long.parseLong(subjectId)).orElseThrow(() -> new IllegalStateException("No such subject found"));
        if (!subject.getUserId().equals(userId))
            throw new IllegalStateException("User is not owner of this subject");

        SubjectModule module = subjectModuleRepo.findById(Long.parseLong(moduleId)).orElseThrow(() -> new IllegalStateException("No such module found"));

        File file = fileRepo.findByNameAndSubjectModule(filename, module);
        if (file == null)
            throw new IllegalStateException("No such file found");

        String nameWithoutExtension = getNameWithoutExtension(filename);
        String filepath = String.format("%s/%s/%s/%s/%s", userId, subject.getName(), module.getName(), nameWithoutExtension, filename);
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

    private static boolean isFileEmpty(MultipartFile file)
    {
        return (file == null || file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty());
    }

    private void saveFileInDatabase(String userId, String subjectName, String subjectAlias, String moduleName, String fileName)
    {
        Long id = Long.parseLong(userId);
        Subject subject = subjectRepo.findByNameAndUserId(subjectName, id);
        if (subject == null)
        {
            subject = new Subject();
            subject.setUserId(id);
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

    String getNameWithoutExtension(String filename)
    {
        if (filename == null)
            return null;
        return filename.substring(0, filename.lastIndexOf("."));
    }

}

