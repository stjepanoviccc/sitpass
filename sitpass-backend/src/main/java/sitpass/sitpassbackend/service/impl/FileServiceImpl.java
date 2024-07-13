package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.service.FileService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LogManager.getLogger(FileServiceImpl.class);

    private final String baseDirectory = "sitpass-bucket";

    @Override
    public String uploadFile(InputStream fileStream, String fileName, String contentType) {
        File directory = new File(baseDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = baseDirectory + File.separator + fileName;
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            logger.info("Uploading file: {}", fileName);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            logger.info("File uploaded successfully: {}", fileName);
            return filePath;
        } catch (IOException e) {
            logger.error("Error uploading file: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public byte[] getImage(String fileName) {
        try {
            Path filePath = Paths.get(baseDirectory, fileName);
            if (!Files.exists(filePath)) {
                logger.error("File does not exist: {}", fileName);
                return getDefaultImage();
            }
            InputStream inputStream = new FileInputStream(filePath.toFile());
            logger.info("Reading file: {}", fileName);
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error("Error retrieving image: {}", e.getMessage(), e);
            return getDefaultImage();
        }
    }

    private byte[] getDefaultImage() {
        return new byte[0];
    }
}