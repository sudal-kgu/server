package store.sonyk9919.api.global.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Component
public class ImageExtensionResolver {

    public String resolve(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            return extractFromFilename(originalFilename);
        }

        return resolveFromMimeType(file.getContentType());
    }

    private String extractFromFilename(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return filename.substring(lastDotIndex);
    }

    private String resolveFromMimeType(String mimeType) {
        if (mimeType == null) throw new CustomException(ErrorStatus.UNSUPPORTED_IMAGE_TYPE, "MIME 타입을 확인할 수 없습니다.");

        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> throw new CustomException(ErrorStatus.UNSUPPORTED_IMAGE_TYPE);
        };
    }
}
