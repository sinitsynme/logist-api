package ru.sinitsynme.logistapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.config.FileProperties;
import ru.sinitsynme.logistapi.exception.service.GetFileFromRootException;
import ru.sinitsynme.logistapi.exception.service.IllegalFileUploadException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {

    private final FileProperties fileProperties;
    private final Path rootLocation;
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    public FileService(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
        rootLocation = Path.of(fileProperties.getBaseDirectory());
    }

    public Resource getResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new GetFileFromRootException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new GetFileFromRootException("Could not read file: " + filename, e);
        }
    }

    public String saveImage(MultipartFile multipartFile) {
        if (multipartFile.getContentType() == null) {
            throw new IllegalFileUploadException("Broken file type!");
        }
        String[] mime = multipartFile.getContentType().split("/");

        String mimeType = mime[0];
        String mimeSubtype = mime[1];

        if (!mimeType.equals("image")) {
            throw new IllegalFileUploadException("File must be an image!");
        }

        if (!fileProperties.getAllowedImageExtensions().contains(mimeSubtype)) {
            throw new IllegalFileUploadException("Image must have one of these extensions: "
                    + fileProperties.getAllowedImageExtensions());
        }

        if (multipartFile.getSize() / 1024 / 1024 > 8) {
            throw new IllegalFileUploadException("Image size must be up to 8 MB!");
        }

        try {
            return saveFile(multipartFile);
        } catch (IOException e) {
            throw new IllegalFileUploadException("Broken file contents!");
        }
    }


    private String saveFile(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalFileUploadException("Image must have a file name!");
        }

        String maskedFileName = UUID.randomUUID() + file.getOriginalFilename();

        Path saveDestination = Path.of(fileProperties.getBaseDirectory() + maskedFileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, saveDestination,
                    StandardCopyOption.REPLACE_EXISTING);

            logger.info("Uploaded file {}", maskedFileName);

            return maskedFileName;

        } catch (IOException e) {
            throw new IllegalFileUploadException("Failed to store file: " + e.getMessage(), e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
