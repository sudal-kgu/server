package store.sonyk9919.api.global.file.serializer;

import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.file.resolver.FilePathResolver;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

@Component
public class ImageSerializer extends ValueSerializer<String> {

    private final FilePathResolver filePathResolver;

    public ImageSerializer(FilePathResolver filePathResolver) { this.filePathResolver = filePathResolver; }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(filePathResolver.resolveOutput(value));
    }
}
