package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.global.common.UriUtils;

@Component
@RequiredArgsConstructor
public class BuildingModelUriResolver {

    private final UriUtils uriUtils;

    public String resolve(BuildingMetadata metadata) {
        return uriUtils.glbUri(metadata.getModel());
    }
}
