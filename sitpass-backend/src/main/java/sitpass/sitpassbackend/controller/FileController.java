package sitpass.sitpassbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sitpass.sitpassbackend.service.FileService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sitpass-bucket")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        byte[] bytes = fileService.getImage(fileName);
        if (bytes != null) {
            return ResponseEntity.ok(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
