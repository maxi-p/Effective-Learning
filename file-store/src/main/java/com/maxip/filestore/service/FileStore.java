package com.maxip.filestore.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@Data
@NoArgsConstructor
public class FileStore
{
    @Autowired
    private AmazonS3 s3;

    public void save(String bucket, String filename, Optional<Map<String, String>> optionalMetadata, InputStream inputStream)
    {
        ObjectMetadata metadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map ->
        {
            if (!map.isEmpty())
            {
                map.forEach(metadata::addUserMetadata);
            }
        });
        try
        {
            s3.putObject(bucket, filename, inputStream, metadata);
        } catch (AmazonServiceException e)
        {
            throw new IllegalStateException("Failed to save file to S3 " + bucket + filename, e);
        }
    }

    public void clearFolder(String bucket, String filename)
    {
        try
        {
            for (S3ObjectSummary file : s3.listObjects(bucket, filename).getObjectSummaries()){
                System.out.println(file.getKey());
                s3.deleteObject(bucket, file.getKey());
            }
        } catch (AmazonServiceException e)
        {
            throw new IllegalStateException("Failed to save file to S3 " + bucket + filename, e);
        }
    }

    public byte[] download(String bucket, String key)
    {
        try
        {
            S3Object code = s3.getObject(bucket, key);
            return IOUtils.toByteArray(code.getObjectContent());
        } catch (AmazonServiceException | IOException e)
        {
            throw new IllegalStateException("Failed to download file from S3: " + bucket + key, e);
        }
    }

    public InputStream downloadIS(String bucket, String key)
    {
        try
        {
            S3Object code = s3.getObject(bucket, key);
            return code.getObjectContent();
        } catch (AmazonServiceException e)
        {
            throw new IllegalStateException("Failed to download file from S3: " + bucket + key, e);
        }
    }
}
