package com.maxip.codeupload.persistence.datastore;

import com.maxip.codeupload.persistence.entity.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore
{
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static
    {
        USER_PROFILES.add(new UserProfile(UUID.fromString("03316f23-0720-4a9d-839e-a2603691924f"), "maksim", "03316f23-0720-4a9d-839e-a2603691924f/e78cc923-c1c5-4635-a96a-3d5fa77a03d3-Main.java" ));
        USER_PROFILES.add(new UserProfile(UUID.fromString("66ceb20b-14cd-48a9-bb84-db89b8010283"), "genjik", null));
    }

    public List<UserProfile> getUserProfiles()
    {
        return USER_PROFILES;
    }
}
