package com.bootcamp.ECommerceApplication.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class ImageUploaderService {
    @Autowired
    @Qualifier("com.cloudinary.cloud_name")
    String mCloudName;

    @Autowired
    @Qualifier("com.cloudinary.api_key")
    String mApiKey;

    @Autowired
    @Qualifier("com.cloudinary.api_secret")
    String mApiSecret;


    //upload User Images
    public String uploadImage(MultipartFile multiPartFile, String imagePublicId)
            throws IOException {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", mCloudName, "api_key", mApiKey, "api_secret", mApiSecret));
        File file = Files.createTempFile("temp", multiPartFile.getOriginalFilename()).toFile();
        multiPartFile.transferTo(file);
        try {
            Map response = cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", imagePublicId, "folder", "/E-commerce/profileImage"));

            return (String) response.get("url");
        } catch (Exception e) {
            throw new IOException("Please try again Later!");
        }
    }
}
