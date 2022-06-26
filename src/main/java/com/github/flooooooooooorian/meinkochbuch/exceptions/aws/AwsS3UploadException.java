package com.github.flooooooooooorian.meinkochbuch.exceptions.aws;

public class AwsS3UploadException extends RuntimeException {
    public AwsS3UploadException(String s) {
        super(s);
    }

    public AwsS3UploadException(String s, Throwable ex) {
        super(s, ex);
    }
}
