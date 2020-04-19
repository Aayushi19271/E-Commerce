package com.bootcamp.ECommerceApplication.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private static Logger logger = LoggerFactory.getLogger(ImageUploaderService.class);


    //upload User Images
    public String uploadUserImage(MultipartFile multiPartFile, String imagePublicId)
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

    //upload Product Variation Images
    public HashSet<String> uploadProductVariationImage(List<MultipartFile> multipartFileList) {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", mCloudName, "api_key", mApiKey, "api_secret", mApiSecret));

        List<Map> resultList = new ArrayList<>();
        multipartFileList.forEach(multipartFile -> {

            File file;
            try {
                file = Files.createTempFile("temp", multipartFile.getOriginalFilename()).toFile();
                multipartFile.transferTo(file);
                Map response = cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", multipartFile.getOriginalFilename(),
                                                                                    "folder", "/E-commerce/profileImage"));
                resultList.add(response);
            }
            catch (IOException e) {
                logger.error(e.getMessage());
            }
        });

        HashSet<String> imageApis = new HashSet<>();
        resultList.forEach(response -> {
            String imageApi = (String) response.get("url");
            imageApis.add(imageApi);
        });
        return imageApis;
    }
}
