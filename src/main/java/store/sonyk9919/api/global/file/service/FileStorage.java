package store.sonyk9919.api.global.file.service;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.file.resolver.FilePathResolver;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorage {

    private final FilePathResolver filePathResolver;

    public void saveFile(MultipartFile image, String fileName) {
        try {
            String path = filePathResolver.resolveInputPath(image, fileName);
            File destination = new File(path);
            image.transferTo(destination);
            log.info("[FileStorage] Saved successfully: {}", path);
        } catch (IOException e) {
            log.error("[FileStorage] Failed to save file: {}", fileName, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}