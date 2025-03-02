package site.walkies.walkie.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/files")
public class FileController {

    // 파일이 저장된 디렉토리
    private final Path rootDir = Paths.get("files");

    // 파일 명으로 파일 호출
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = rootDir.resolve(filename).normalize();
            Resource resource = new FileSystemResource(filePath);

            // 파일 존재 여부 확인
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            String contentType = Files.probeContentType(filePath);
            if (!StringUtils.hasText(contentType)) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
