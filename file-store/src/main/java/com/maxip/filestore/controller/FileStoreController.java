package com.maxip.filestore.controller;

import com.maxip.filestore.entity.File;
import com.maxip.filestore.entity.ModuleResponse;
import com.maxip.filestore.entity.Subject;
import com.maxip.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/file-store")
public class FileStoreController
{
    @Autowired
    private FileStoreService fileStoreService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadUserProfileImage(MultipartFile file, @RequestParam("subject") String subject, @RequestParam("alias") Optional<String> alias, @RequestParam("module") String module, @RequestHeader("loggedId") String id)
    {
        String strAlias = alias.orElse("");
        fileStoreService.uploadFile(id, subject, strAlias, module, file);
    }

    @GetMapping(path = "/images/{subjectId}/{moduleId}/{filename}")
    public ResponseEntity<Resource> downloadImages(@PathVariable("subjectId") String subjectId, @PathVariable("moduleId") String moduleId, @PathVariable("filename") String filename, @RequestHeader("loggedId") String loggedId) throws IOException
    {
        byte[] image = fileStoreService.download(loggedId, subjectId, moduleId, filename);
        ByteArrayResource resource = new ByteArrayResource(image);
        String contentType = "image/jpeg";

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build().toString())
                .body(resource);
    }

    @GetMapping(path = "/file/{subjectId}/{moduleId}/{filename}")
    public ResponseEntity<Resource> download(@PathVariable("subjectId") String subjectId, @PathVariable("moduleId") String moduleId, @PathVariable("filename") String filename, @RequestHeader("loggedId") String loggedId) throws IOException
    {
        byte[] array = fileStoreService.download(loggedId, subjectId, moduleId, filename);

        ByteArrayResource resource = new ByteArrayResource(array);
        String contentType = "application/pdf";
        System.out.println("Getting pdf name: " + filename);
        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build().toString())
                .body(resource);
    }

    @DeleteMapping(path = "/file/{subjectId}/{moduleId}/{filename}")
    public ResponseEntity<File> deleteFile(
            @PathVariable("subjectId") String subjectId,
            @PathVariable("moduleId") String moduleId,
            @PathVariable("filename") String filename,
            @RequestHeader("loggedId") String loggedId) throws IOException
    {
        File deletedFile = fileStoreService.deleteFile(loggedId, subjectId, moduleId, filename);
        return ResponseEntity.ok(deletedFile);
    }

    @GetMapping(path = "/subjects")
    public List<Subject> readSubjects(@RequestHeader("loggedId") String id)
    {
        return fileStoreService.getAllSubjects(id);
    }

    @GetMapping(path = "/subjects/{subjectId}")
    public List<ModuleResponse> readModules(@RequestHeader("loggedId") String id, @PathVariable("subjectId") String subjectId)
    {
        return fileStoreService.getAllModules(id, subjectId);
    }
}
