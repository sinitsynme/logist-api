package ru.sinitsynme.logistapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String getLinkToResource(String fileName);

    String saveImage(MultipartFile multipartFile);

    void deleteImage(String fileName);
}
