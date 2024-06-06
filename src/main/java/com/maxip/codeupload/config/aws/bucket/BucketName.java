package com.maxip.codeupload.config.aws.bucket;

import org.springframework.core.env.Environment;

public enum BucketName
{
    CODE_UPLOAD("bucket-name");

    private Environment environment;

    private final String bucketName;

    BucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getBucketName()
    {
        return bucketName;
    }
}
