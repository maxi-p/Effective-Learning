package com.maxip.codeupload.controller;

import com.maxip.codeupload.persistence.entity.Privilege;
import com.maxip.codeupload.persistence.entity.Role;
import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("api/v1")
@CrossOrigin("http://localhost:5173/")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping(value = "/adduUser")
    public HttpStatus insertUser()
    {
        User user = new User();
        Privilege privilege = new Privilege();
        Role role = new Role();
        user.setUsername("max");
        privilege.setName("write");
        role.setName("admin");

        role.setPrivileges(Arrays.asList(privilege));
        user.setRoles(Arrays.asList(role));


        return userService.addUser(user) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
    }
//    private final UserProfileService userProfileService;
//
//    @Autowired
//    public UserController(UserProfileService userProfileService)
//    {
//        this.userProfileService = userProfileService;
//    }

//    @GetMapping("/user-profile")
//    public List<User> getUserProfiles()
//    {
//        return userProfileService.getAllUserProfiles();
//    }
//
//    @PostMapping(path = "{userProfileId}/file/upload",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId, MultipartFile file)
//    {
//        userProfileService.uploadUserProfileImage(userProfileId, file);
//    }
//    @GetMapping(path = "{userProfileId}/file/download")
//    public byte[] downloadUsersLatestCode(@PathVariable("userProfileId") UUID userProfileId)
//    {
//        return userProfileService.downloadLatestCode(userProfileId);
//    }
}
