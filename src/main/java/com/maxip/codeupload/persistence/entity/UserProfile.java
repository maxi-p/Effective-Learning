package com.maxip.codeupload.persistence.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// For now there is not such thing as an @Entity...
public class UserProfile
{
    private UUID uuid;
    private String username;
    private String userProfileImageLink;

    public UserProfile(UUID uuid, String username, String userProfileImageLink)
    {
        this.uuid = uuid;
        this.username = username;
        this.userProfileImageLink = userProfileImageLink;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Optional<String> getUserProfileImageLink()
    {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink)
    {
        this.userProfileImageLink = userProfileImageLink;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(username, that.username) &&
                Objects.equals(userProfileImageLink, that.userProfileImageLink);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, username, userProfileImageLink);
    }
}
