package ru.sinitsynme.logistapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.config.FileProperties;
import ru.sinitsynme.logistapi.config.ObjectStorageProperties;

@Component
public class FileServiceFacade implements FileService {
    private final FileProperties fileProperties;
    private final ObjectStorageProperties objectStorageProperties;
    private final FileService fileService;

    public FileServiceFacade(FileProperties fileProperties,
                             ObjectStorageProperties objectStorageProperties,
                             @Value("#{new Boolean('${feature-flags.s3-on}')}") boolean fileFeatureFlag
    ) {
        this.fileProperties = fileProperties;
        this.objectStorageProperties = objectStorageProperties;

        if (fileFeatureFlag) {
            this.fileService = new FileS3Service(objectStorageProperties, fileProperties);
        } else this.fileService = new FileServiceImpl(fileProperties);
    }

    @Override
    public String getLinkToResource(String fileName) {
        return fileService.getLinkToResource(fileName);
    }

    @Override
    public String saveImage(MultipartFile multipartFile) {
        return fileService.saveImage(multipartFile);
    }
}
