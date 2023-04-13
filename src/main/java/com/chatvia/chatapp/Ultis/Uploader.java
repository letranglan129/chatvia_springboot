package com.chatvia.chatapp.Ultis;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Uploader {
    private Cloudinary cloudinary;

    public Uploader() {
        Map config = new HashMap();
        config.put("cloud_name", "dtgkkyqm6");
        config.put("api_key", "424167362197437");
        config.put("api_secret", "tLwWgeGnZi-nbqglTJt7_WS0mEE");
        this. cloudinary = new Cloudinary(config);
    }

    public Map uploadPath(String path) throws IOException {
        try {
            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("folder", "chatvia_uploads_php");
            uploadOptions.put("resource_type", "auto");
            Map uploadResult = cloudinary.uploader().upload(path, uploadOptions);
            return uploadResult;
        } catch (Exception e) {
            return null;
        }
    }
}
