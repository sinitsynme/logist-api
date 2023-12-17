package ru.sinitsynme.logistapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.sinitsynme.logistapi.config.FileProperties;
import ru.sinitsynme.logistapi.config.ObjectStorageProperties;
import ru.sinitsynme.logistapi.exception.service.IllegalFileUploadException;

import java.io.IOException;
import java.util.UUID;

public class FileS3Service implements FileService {

    private final FileProperties fileProperties;
    private final ObjectStorageProperties objectStorageProperties;
    private final AmazonS3 s3Client;
    private final Logger logger = LoggerFactory.getLogger(FileS3Service.class);

    public FileS3Service(ObjectStorageProperties objectStorageProperties, FileProperties fileProperties) {
        this.fileProperties = fileProperties;
        this.objectStorageProperties = objectStorageProperties;
        s3Client = initS3Client();
    }

    @Override
    public String getLinkToResource(String fileName) {
        return s3Client.getUrl(objectStorageProperties.getBucketName(), fileName).toExternalForm();
    }

    @Override
    public String saveImage(MultipartFile file) {
        if (file.getContentType() == null) {
            throw new IllegalFileUploadException("Broken file type!");
        }
        String[] mime = file.getContentType().split("/");

        String mimeType = mime[0];
        String mimeSubtype = mime[1];

        if (!mimeType.equals("image")) {
            throw new IllegalFileUploadException("File must be an image!");
        }

        if (!fileProperties.getAllowedImageExtensions().contains(mimeSubtype)) {
            throw new IllegalFileUploadException("Image must have one of these extensions: "
                    + fileProperties.getAllowedImageExtensions());
        }

        if (file.getSize() / 1024 / 1024 > 8) {
            throw new IllegalFileUploadException("Image size must be up to 8 MB!");
        }

        try {
            return saveFile(file);
        } catch (IOException e) {
            throw new IllegalFileUploadException("Something went wrong with AWS: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String maskedFileName = UUID.randomUUID() + file.getOriginalFilename();

        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(file.getContentType());
        data.setContentLength(file.getSize());

        s3Client.putObject(
                objectStorageProperties.getBucketName(),
                maskedFileName,
                file.getInputStream(),
                data);

        logger.info("Saved file: {}", maskedFileName);
        return maskedFileName;
    }

    private AmazonS3 initS3Client() {
        final AmazonS3 s3Client;

        AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                objectStorageProperties.getAccessKey(),
                objectStorageProperties.getSecretKey()
        ));

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                objectStorageProperties.getEndpoint(),
                objectStorageProperties.getAwsRegion()
        );

        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentials)
                .build();

        return s3Client;
    }
}
