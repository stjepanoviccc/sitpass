package sitpass.sitpassbackend.service;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(InputStream stream, String path, String contentType);

    byte[] getImage(String fileName) throws IOException;
}