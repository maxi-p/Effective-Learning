package com.maxip.codeupload.persistence.repository;

import com.maxip.codeupload.persistence.datastore.FakeUserProfileDataStore;
import com.maxip.codeupload.persistence.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService
{

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore)
    {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    public List<UserProfile> getUserProfiles()
    {
        return fakeUserProfileDataStore.getUserProfiles();
    }
}
