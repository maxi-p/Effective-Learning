package com.maxip.codeupload.service;

import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean addUser(User person)
    {
        return userRepository.save(person) != null;
    }

    @Transactional
    public Set<User> getAllUsers()
    {
        return (Set<User>) userRepository.findAll();
    }
}
