package com.example.login_logout.member.handler;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class ImageHandler {
    public String save(MultipartFile image) throws IOException {
        String fileName = getFileName(image);
        String uploadDir = "src/main/resources/img/";
        String fullPathName = uploadDir + fileName;

        System.out.println("uploadDir : " + uploadDir);
        System.out.println("fullPathName : " + fullPathName);

        Path path = Paths.get(fullPathName);
        Files.write(path, image.getBytes());

        return fullPathName;
    }

    private String getFileName(MultipartFile image) {
        return UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
    }
}

