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

        if (!file.getContentType().equals("application/pdf"))
            throw new IllegalStateException("Only pdf files are supported for now");

        Map<String, String> metadata = getMetaData(file);

        String bucket = amazonConfig.BUCKET_NAME;
        String filename = file.getOriginalFilename().replace(".pdf", "");
        String filePrefix = String.format("%s/%s/%s/%s", userId, subject, module, filename);
        String filepath = String.format("%s/%s.pdf", filePrefix, filename);
        List<ByteArrayInputStream> images = new ArrayList<>();

        try (PDDocument pdf = PDDocument.load(file.getInputStream());)
        {
            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            int pages = pdf.getNumberOfPages();
            safeFileInDatabase(Long.parseLong(userId), subject, alias, module, pages, file.getOriginalFilename()); // Throws an exception if the file already exists (duplicate name)
            for (int i = 0; i < pages; i++)
            {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", outputStream);
                ByteArrayInputStream inputStream  = new ByteArrayInputStream(outputStream.toByteArray());
                images.add(inputStream);
            }
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Failed to split PDF", e);
        }

        try
        {
            fileStore.save(bucket, filepath, Optional.of(metadata), file.getInputStream());
            Map<String, String> md = new HashMap<>();
            for (int i=0; i<images.size(); i++)
            {
                ByteArrayInputStream image = images.get(i);
                String imagePath = String.format("%s/%s_page-%s.jpg", filePrefix, filename, i);
                fileStore.save(bucket, imagePath, Optional.of(md), image);
            }
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
        {
            throw new IllegalStateException("User is not owner of this subject");
        }

        SubjectModule module = subjectModuleRepo.findById(Long.parseLong(moduleId)).orElseThrow(() -> new IllegalStateException("No such module found"));
        String nameWithoutExtension = getNameWithoutExtension(filename);
        File file = fileRepo.findByNameAndSubjectModule(nameWithoutExtension+".pdf", module);
        if (file == null)
        {
            throw new IllegalStateException("No such file found");
        }

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

    private void safeFileInDatabase(Long userId, String subjectName, String subjectAlias, String moduleName, int numberOfPages, String fileName)
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
        file.setNumberOfPages(numberOfPages);
        file.setSubjectModule(module);
        fileRepo.save(file);
    }

    String getNameWithoutExtension(String filename)
    {
        filename = filename.substring(0, filename.lastIndexOf("."));
        if (filename.lastIndexOf("_page-") == -1)
            return filename;
        return filename.substring(0, filename.lastIndexOf("_page-"));
    }

}

