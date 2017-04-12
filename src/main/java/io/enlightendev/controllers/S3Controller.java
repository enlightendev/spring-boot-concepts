package io.enlightendev.controllers;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.enlightendev.service.impl.S3Service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private S3Service s3Service;

    @RequestMapping("/list")
    public List<Bucket> listBuckets() {

        log.debug("listing s3 buckets");
        return s3Service.listBuckets();
    }

    @RequestMapping("/list/{bucket}")
    public List<S3ObjectSummary> listObjects(@PathVariable String bucket) {

        ObjectListing objectListing = s3Service.listBucketObjects(bucket);

        return objectListing.getObjectSummaries();
    }

    @RequestMapping(value = "/create/{bucketName}", method = RequestMethod.POST)
    public String createBucket(@PathVariable String bucketName) {

        Bucket bucket = s3Service.createBucket(bucketName);

        return bucket.toString();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String writeSomething() throws IOException {

        s3Service.writeResource("thisismyfile.txt", "content".getBytes());

        return "done";
    }

}