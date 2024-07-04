package com.maxip.filestore.controller;

import com.maxip.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/file-store")
public class FileStoreController
{
    @Autowired
    private FileStoreService fileStoreService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadUserProfileImage(MultipartFile file, @RequestParam("subject") String subject, @RequestParam("module") String module, @RequestHeader("loggedId") String id)
    {
        fileStoreService.uploadFile(id, subject, module, file);
    }

    @GetMapping(path = "/{filename}")
    public byte[] downloadUsersLatestCode(@PathVariable("filename") String filename)
    {
        return fileStoreService.downloadFile(filename);
    }
}
