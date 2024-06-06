package com.maxip.codeupload.controller;

import com.maxip.codeupload.persistence.entity.UserProfile;
import com.maxip.codeupload.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
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
}
