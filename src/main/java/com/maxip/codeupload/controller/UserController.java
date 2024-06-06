package com.maxip.codeupload.controller;

import com.maxip.codeupload.persistence.entity.UserProfile;
import com.maxip.codeupload.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("http://localhost:5173/")
public class UserController
{
    private final UserProfileService userProfileService;

    @Autowired
    public UserController(UserProfileService userProfileService)
    {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/user-profile")
    public List<UserProfile> getUserProfiles()
    {
        return userProfileService.getAllUserProfiles();
    }

    @PostMapping(path = "{userProfileId}/file/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId, MultipartFile file)
    {
        userProfileService.uploadUserProfileImage(userProfileId, file);
    }
}
