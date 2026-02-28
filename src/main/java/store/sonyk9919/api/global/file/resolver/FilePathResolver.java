package store.sonyk9919.api.global.file.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import store.sonyk9919.api.global.file.constants.FileDirectory;

import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class FilePathResolver {

    private final ImageExtensionResolver imageExtensionResolver;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Value("${file.external-path}")
    private String EXTERNAL_PATH;

    public String resolveInputPath(MultipartFile image, String name) {
        String ext = imageExtensionResolver.resolve(image);
        return Paths.get(UPLOAD_DIR, FileDirectory.INPUTS.toString(), name + ext)
                .toString();
    }

    public String resolveOutput(String name) {
        return Paths.get(EXTERNAL_PATH, name)
                .toString();
    }
}
