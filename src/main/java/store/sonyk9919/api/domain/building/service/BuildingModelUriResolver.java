package store.sonyk9919.api.domain.building.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;

@Component
public class BuildingModelUriResolver {

    @Value("${image.base-url}")
    private String imageBaseUrl;

    public String resolve(BuildingMetadata metadata) {
        return String.format("%s/models/%s.glb", imageBaseUrl, metadata.getModel());
    }
}
