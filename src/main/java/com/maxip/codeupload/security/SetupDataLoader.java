package com.maxip.codeupload.security;

import java.util.*;

import com.maxip.codeupload.persistence.entity.*;
import com.maxip.codeupload.persistence.repository.springdatajpa.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>
{
    public static final String DELETE_PRIVILEGE     = "DELETE_PRIVILEGE";
    public static final String WRITE_PRIVILEGE      = "WRITE_PRIVILEGE";
    public static final String READ_PRIVILEGE       = "READ_PRIVILEGE";
    public static final String ROLE_USER            = "ROLE_USER";
    public static final String ROLE_ADMIN           = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR       = "ROLE_MODERATOR";
    public static final String EASY                 = "EASY";
    public static final String MEDIUM               = "MEDIUM";
    public static final String HARD                 = "HARD";
    public static final String JAVA_API             = "JAVA_API";
    public static final String DYNAMIC_PROGRAMMING  = "DYNAMIC_PROGRAMMING";
    public static final String TREES                = "TREES";

    private boolean isAlreadyConfigured;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private AlgorithmRepository algorithmRepository;
    @Autowired
    private AlgorithmSubStepRepository algorithmSubStepRepository;
    @Autowired
    private CodingProblemRepository codingProblemRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private SubjectCategoryRepository subjectCategoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (isAlreadyConfigured)
        {
            return;
        }

        // Users (Admin, Moderator, and User)

        Privilege readPrivilege = createPrivilegeIfNotFound(READ_PRIVILEGE);
        Privilege writePrivilege = createPrivilegeIfNotFound(WRITE_PRIVILEGE);
        Privilege deletePrivilege = createPrivilegeIfNotFound(DELETE_PRIVILEGE);

        Set<Privilege> adminPrivileges  = new HashSet<>(Arrays.asList(readPrivilege, writePrivilege, deletePrivilege));
        Set<Privilege> managerPrivileges= new HashSet<>(Arrays.asList(readPrivilege, writePrivilege));
        Set<Privilege> userPrivileges   = new HashSet<>(Arrays.asList(readPrivilege));

        Role adminRole = createRoleIfNotFound(ROLE_ADMIN, adminPrivileges);
        Role moderatorRole = createRoleIfNotFound(ROLE_MODERATOR, managerPrivileges);
        Role userRole = createRoleIfNotFound(ROLE_USER, userPrivileges);


        User max = createUserIfNotFound("Max", "Petrushin", "maxi-p", adminRole, "asd");
        createUserIfNotFound("Gena", "Matchanov", "genjik", moderatorRole, "asd");
        createUserIfNotFound("Islam", "Djalgasbaev","id13", userRole, "asd");

        // Business Logic

        Difficulty easy = createDifficultyIfNotFound(EASY);
        Difficulty medium = createDifficultyIfNotFound(MEDIUM);
        Difficulty hard = createDifficultyIfNotFound(HARD);

        SubjectCategory java_api = createSubjectCategoryIfNotFound(JAVA_API);
        SubjectCategory dynamic_programming = createSubjectCategoryIfNotFound(DYNAMIC_PROGRAMMING);
        SubjectCategory trees = createSubjectCategoryIfNotFound(TREES);

        Note n1 = createNoteIfNotFound("str.chartAt( index);","returns a primitive char at that index of a string", max, java_api);

        CodingProblem p1 = createCodingProblemIfNotFound("efficient cost dp", max, Collections.singletonList(dynamic_programming), hard);
        CodingProblem p2 = createCodingProblemIfNotFound("wave frequency maximum message distance", max, Collections.singletonList(trees), hard);

        Algorithm a1 = createAlgorithmIfNotFound("partition subarray","finding the partition subarray",p1);
        Algorithm a2 = createAlgorithmIfNotFound("longest path between nodes","find the longest simple path between two nodes which is called the diameter if the graph is the tree",p2);

        AlgorithmSubStep s1 = createAlgorithmSubStepIfNotFound(a1, 1L, "walk the array");
        AlgorithmSubStep s2 = createAlgorithmSubStepIfNotFound(a1, 2L, "you have a choise wether to place the separator AFTER the current index or not");
        AlgorithmSubStep s3 = createAlgorithmSubStepIfNotFound(a1, 3L, "Follow the logic of the problem to calculate all the \"inbetweens\" at the moment of making that decision");

        AlgorithmSubStep s4 = createAlgorithmSubStepIfNotFound(a2, 1L, "do a DFS function to calculate height");
        AlgorithmSubStep s5 = createAlgorithmSubStepIfNotFound(a2, 2L, "for each node (starting at root) iterate through its children ");
        AlgorithmSubStep s6 = createAlgorithmSubStepIfNotFound(a2, 3L, "for each child call a recursive height");
        AlgorithmSubStep s7 = createAlgorithmSubStepIfNotFound(a2, 4L, "At the end of iterating calculate the diameter from the 2 largest heights of the children subtrees");
        AlgorithmSubStep s8 = createAlgorithmSubStepIfNotFound(a2, 5L, "Also, have the global variable that stores the maximum diameter of all subtrees");

        isAlreadyConfigured = true;
    }

    @Transactional
    private User createUserIfNotFound(String firstname, String lastname, String username, Role role, String password)
    {
        User user = userRepository.findByUsername(username);

        if (user == null)
        {
            user = new User();
            user.setUsername(username);
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(new HashSet<>(Arrays.asList(role)));
            user.setEnabled(true);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name)
    {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null)
        {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Set<Privilege> privileges)
    {
        Role role = roleRepository.findByName(name);
        if (role == null)
        {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    private Difficulty createDifficultyIfNotFound(String name)
    {
        Difficulty difficulty = difficultyRepository.findByName(name);
        if (difficulty == null)
        {
            difficulty = new Difficulty();
            difficulty.setName(name);
            difficultyRepository.save(difficulty);
        }
        return difficulty;
    }

    @Transactional
    private SubjectCategory createSubjectCategoryIfNotFound(String name)
    {
        SubjectCategory subjectCategory = subjectCategoryRepository.findByName(name);
        if (subjectCategory == null)
        {
            subjectCategory = new SubjectCategory();
            subjectCategory.setName(name);
            subjectCategoryRepository.save(subjectCategory);
        }
        return subjectCategory;
    }

    @Transactional
    private Note createNoteIfNotFound(String key, String value, User user, SubjectCategory subjectCategory)
    {
        Note note = noteRepository.findByKeyAndValue(key, value);
        if (note == null)
        {
            note = new Note();
            note.setKey(key);
            note.setValue(value);
            note.setUser(user);
            note.setSubjectCategory(subjectCategory);
            noteRepository.save(note);
        }
        return note;
    }

    @Transactional
    private CodingProblem createCodingProblemIfNotFound(String name, User user, List<SubjectCategory> subjectCategory, Difficulty difficulty)
    {
        CodingProblem problem = codingProblemRepository.findByNameAndUser(name, user);
        if (problem == null)
        {
            problem = new CodingProblem();
            problem.setName(name);
            problem.setUser(user);
            problem.setSubjectCategory(subjectCategory);
            problem.setDifficulty(difficulty);
            problem.setAlgorithms(new ArrayList<>());
            codingProblemRepository.save(problem);
        }
        return problem;
    }

    @Transactional
    private Algorithm createAlgorithmIfNotFound(String name, String description, CodingProblem codingProblem)
    {
        Algorithm algorithm = algorithmRepository.findByName(name);
        if (algorithm == null)
        {
            algorithm = new Algorithm();
            algorithm.setName(name);
            algorithm.setDescription(description);
            algorithm.setAlgorithmSubSteps(new ArrayList<>());
            algorithm = algorithmRepository.save(algorithm);
            codingProblem.getAlgorithms().add(algorithm);
        }
        return algorithm;
    }

    @Transactional
    private AlgorithmSubStep createAlgorithmSubStepIfNotFound(Algorithm algorithm, Long stepNumber, String description)
    {
        AlgorithmSubStep algorithmSubStep = algorithmSubStepRepository.findByDescription(description);
        if (algorithmSubStep == null)
        {
            algorithmSubStep = new AlgorithmSubStep();
            algorithmSubStep.setStepNumber(stepNumber);
            algorithmSubStep.setDescription(description);
            algorithmSubStep = algorithmSubStepRepository.save(algorithmSubStep);
            algorithm.getAlgorithmSubSteps().add(algorithmSubStep);
        }
        return algorithmSubStep;
    }
}
