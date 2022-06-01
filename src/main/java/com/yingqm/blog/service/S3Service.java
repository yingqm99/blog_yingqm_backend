package com.yingqm.blog.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.yingqm.blog.util.BlogGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class S3Service {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    private BlogGenerator blogGenerator;

    @Value("${bucket-name}")
    private String bucketName;

    public boolean addBlog(String title, String userName, String text) {
        blogGenerator.generateFile(title, userName, text);
        try {
            s3.putObject(bucketName, title + "." + userName,
                    new File(title + "." + userName + ".md"));
        } catch (AmazonServiceException e) {
            log.error("S3 FILE UPLOAD FAILED");
            System.exit(1);
            return false;
        }
        return true;
    }

    public String readBlog(String title, String userName) {
        StringBuffer blogText = new StringBuffer();
        try {
            S3Object o = s3.getObject(bucketName, "aaa.test1");
            S3ObjectInputStream s3is = o.getObjectContent();
            // FileOutputStream fos = new FileOutputStream(new File("copy.md"));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                blogText.append(read_buf.toString());
                // fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            // fos.close();
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
        return blogText.toString();
    }

    public boolean deleteBlog(String title, String userName) {
        try {
            s3.deleteObject(bucketName, title + "." + userName);
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        }
        return true;
    }

    public boolean updateBlog(String title, String userName, String text) {
        if (!deleteBlog(title, userName) || !addBlog(title, userName, text)) {
            return false;
        }
        return false;
    }
}
