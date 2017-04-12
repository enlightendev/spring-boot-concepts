package io.enlightendev.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@ConfigurationProperties(prefix = "service-s3")
public class S3Service {

    private String rootBucket;

    @Autowired
    private AmazonS3Client s3;

    @Autowired
    private ResourceLoader resourceLoader;

    private TransferManager transferManager;

    public S3Service() {
        transferManager = new TransferManager(this.s3);
    }

    public String getRootBucket() {
        return rootBucket;
    }

    public void setRootBucket(String rootBucket) {
        this.rootBucket = rootBucket;
    }

    public List<Bucket> listBuckets() {

        List<Bucket> buckets = s3.listBuckets();

        return buckets;
    }

    public Bucket createBucket(String bucketName) {

        Bucket bucket = s3.createBucket(bucketName);

        return bucket;

    }

    public ObjectListing listBucketObjects(String bucket) {

        ObjectListing objectListing = s3.listObjects(bucket);

        return objectListing;
    }

    /**
     * @throws IOException
     */
    public void writeResource(String fileName, byte[] fileContents) throws IOException {

        //TODO: property file
        Resource resource = this.resourceLoader.getResource(rootBucket + fileName);

        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(fileContents);
        }
    }

    /**
     *
     * @param file
     */
    public void storeFile(File file, String fileName) {

        try {
            AmazonS3 amazonS3 = transferManager.getAmazonS3Client();
            if (amazonS3 == null) {
                transferManager = new TransferManager(this.s3);
            }
            Upload upload = transferManager.upload(rootBucket, fileName, file);

            while (upload.isDone() == false) {
                System.out.println("Transfer: " + upload.getDescription());
                System.out.println("  - State: " + upload.getState());
                System.out.println("  - Progress: " + upload.getProgress().getBytesTransferred());
            }

        } catch (AmazonServiceException awss) {
            System.out.println(awss.getErrorCode());
        } catch (AmazonClientException awsc) {
            System.out.println(awsc.getMessage());
        }
    }

}