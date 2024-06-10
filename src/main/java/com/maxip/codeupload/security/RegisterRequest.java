package com.maxip.codeupload.security;

public class RegisterRequest
{
    private String firstname;
    private String lastname;
    private String username;
    private String password;

    public RegisterRequest()
    {
    }

    public RegisterRequest(String firstname, String lastname, String username, String password)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public String getFirstName()
    {
        return firstname;
    }

    public void setFirstName(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastName()
    {
        return lastname;
    }

    public void setLastName(String lastname)
    {
        this.lastname = lastname;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
