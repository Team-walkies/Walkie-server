package site.walkies.walkie.global.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {

    // 파일 저장 경로
    private final String uploadDir = "files/";

    // 파일 저장 method
    // input : file
    // output : 파일 경로 + 파일명
    public String saveFile(MultipartFile file){

        // 원본 파일명에서 확장자 추출
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex != -1) {
            extension = originalFileName.substring(dotIndex);
        }

        // 시간 값 사용
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // 랜덤값 생성
        String randomPrefix = UUID.randomUUID().toString();

        // 파일이름 + 현재 시각을 파일명으로 사용
        String fileName = randomPrefix + timestamp + extension;


        // 해당 경로의 directory
        Path directory = Paths.get(uploadDir);

        // 파일 저장
        Path filePath = directory.resolve(fileName);
        try{
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
        }


        // 경로 + 파일명 리턴
        return uploadDir + fileName;
    }
}
