package com.maxip.codeupload.config.aws.bucket;

public enum BucketName
{
    CODE_UPLOAD("bucket-name");

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
