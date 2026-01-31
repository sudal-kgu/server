package store.sonyk9919.api.global.file;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorage {

    private final FileExtensionResolver extensionResolver;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public void saveFile(MultipartFile image, String fileName) {
        try {
            String extension = extensionResolver.resolve(image);
            File destination = new File(UPLOAD_DIR + fileName + extension);
            image.transferTo(destination);
            log.info("[FileStorage] Saved successfully: {}{}", fileName, extension);
        } catch (IOException e) {
            log.error("[FileStorage] Failed to save file: {}", fileName, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}