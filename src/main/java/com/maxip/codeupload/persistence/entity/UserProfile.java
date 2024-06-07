package com.maxip.codeupload.persistence.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// For now there is not such thing as an @Entity...
public class UserProfile
{
    private final UUID uuid;
    private final String username;
    private String userProfileLatestCode;

    public UserProfile(UUID uuid, String username, String userProfileLatestCode)
    {
        this.uuid = uuid;
        this.username = username;
        this.userProfileLatestCode = userProfileLatestCode;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public String getUsername()
    {
        return username;
    }

    public Optional<String> getUserProfileLatestCode()
    {
        return Optional.ofNullable(userProfileLatestCode);
    }

    public void setUserProfileLatestCode(String userProfileLatestCode)
    {
        this.userProfileLatestCode = userProfileLatestCode;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(username, that.username) &&
                Objects.equals(userProfileLatestCode, that.userProfileLatestCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(uuid, username, userProfileLatestCode);
    }
}
