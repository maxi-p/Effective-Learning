package com.maxip.codeupload.service;

import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.filestore.FileStore;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import org.apache.http.entity.ContentType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

@Service
@PropertySource("classpath:credentials/aws.properties")
public class UserProfileService
{
//    private final String BUCKET_NAME = "bucketName";
//
//    private final UserRepository userRepository;
//    private final FileStore fileStore;
//    private final Environment environment;
//
//    @Autowired
//    public UserProfileService(UserRepository userRepository,
//                              Environment environment,
//                              FileStore fileStore)
//    {
//        this.userRepository = userRepository;
//        this.environment = environment;
//        this.fileStore = fileStore;
//    }

//    public List<User> getAllUserProfiles()
//    {
//        return userRepository.getUserProfiles();
//    }

//    public void uploadUserProfileImage(UUID uuid, MultipartFile file)
//    {
//        isFileEmpty(file);
//        isRightMimeType(file);
//        User user = getUserOrExcept(uuid); // Temporary in-memory "database" check
//        Map<String, String> metadata = getMetaData(file);
//
//        String path     = environment.getProperty(BUCKET_NAME);
//        String filename = String.format("%s/%s-%s", user.getUuid(), UUID.randomUUID(), file.getOriginalFilename());
//
//        try
//        {
//            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
//            user.setUserProfileLatestCode(filename);
//        }
//        catch (IOException e)
//        {
//            throw new IllegalStateException("Something is wrong with the file stream: ", e);
//        }
//    }

//    public byte[] downloadLatestCode(UUID uuid)
//    {
//        User user = getUserOrExcept(uuid);
//        String bucket = environment.getProperty(BUCKET_NAME);
//
//        return user.getUserProfileLatestCode()
//                .map(key -> fileStore.download(bucket,key))
//                .orElse(new byte[0]);
//    }

//    private static Map<String, String> getMetaData(MultipartFile file)
//    {
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("Content-Type", file.getContentType());
//        metadata.put("Content-Length", file.getContentType());
//        return metadata;
//    }

//    private User getUserOrExcept(UUID uuid)
//    {
//        return userRepository
//                .getUserProfiles()
//                .stream()
//                .filter(userProfile -> userProfile.getUuid().equals(uuid))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("User profile not found for uuid: " + uuid));
//    }

//    private static void isRightMimeType(MultipartFile file)
//    {
//        if (!Arrays.asList(ContentType.APPLICATION_OCTET_STREAM.getMimeType(),
//                            ContentType.TEXT_PLAIN.getMimeType()).contains(file.getContentType()))
//        {
//            throw new IllegalArgumentException("File must be a source code file, but is: "+file.getContentType());
//        }
//    }

//    private static void isFileEmpty(MultipartFile file)
//    {
//        if (file == null && file.isEmpty())
//        {
//            throw new IllegalArgumentException("File is null or empty");
//        }
//    }
}
