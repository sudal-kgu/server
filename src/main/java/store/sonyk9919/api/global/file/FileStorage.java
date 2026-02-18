package store.sonyk9919.api.global.file;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.domain.analysis.dto.DetectedItem;
import store.sonyk9919.api.domain.analysis.dto.request.AnalysisRequestDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.file.constants.FileDirectory;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStorage {

    private final ImageExtensionResolver extensionResolver;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    public void saveFile(MultipartFile image, String fileName) {
        try {
            String extension = extensionResolver.resolve(image);
            String file = fileName + extension;

            File destination = new File(UPLOAD_DIR + FileDirectory.INPUTS + file);
            image.transferTo(destination);
            log.info("[FileStorage] Saved successfully: {}{}", fileName, extension);
        } catch (IOException e) {
            log.error("[FileStorage] Failed to save file: {}", fileName, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteUnSelectFiles(AnalysisRequestDto requestDto) {
        File directory = new File(UPLOAD_DIR + FileDirectory.OUTPUTS + requestDto.getRequestId() + "/");

        if (!directory.exists() || !directory.isDirectory()) {
            log.warn("[FileStorage] Target directory does not exist: {}", directory.getAbsolutePath());
            return;
        }

        Set<String> selectedFileNames = extractSelectedFileNames(requestDto.getDetectedItems());
        deleteFiles(directory, selectedFileNames);
    }

    private Set<String> extractSelectedFileNames(List<DetectedItem> detectedItems) {
        if (detectedItems == null) {
            return Collections.emptySet();
        }
        return detectedItems.stream()
                .map(DetectedItem::getFilename)
                .collect(Collectors.toSet());
    }

    private void deleteFiles(File directory, Set<String> selectedFileNames) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && !selectedFileNames.contains(file.getName())) {
                if(file.delete()){
                    continue;
                }
                log.warn("[FileStorage] Failed to delete file: {}", file.getName());
            }
        }
    }
}
