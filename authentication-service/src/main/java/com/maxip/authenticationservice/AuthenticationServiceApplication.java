package com.maxip.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class AuthenticationServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

}
