package com.maxip.codeupload.service;

import com.maxip.codeupload.persistence.entity.UserProfile;
import com.maxip.codeupload.persistence.repository.UserProfileDataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService
{
    private final UserProfileDataAccessService userProfileDataAccessService;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService)
    {
        this.userProfileDataAccessService = userProfileDataAccessService;
    }

    public List<UserProfile> getAllUserProfiles()
    {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID uuid, MultipartFile file)
    {

    }
}
